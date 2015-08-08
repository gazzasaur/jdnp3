/**
 * Copyright 2015 Graeme Farquharson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jdnp3.dnp3.stack.layer.transport;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;

public class TransportLayerImpl implements TransportLayer {
	private int maximumTransmissionUnit = 250;
	private int maximumReceiveDataSize = 10000;
	private DataLinkLayer dataLinkLayer = null;
	private ApplicationLayer applicationLayer = null;

	private int lastSequenceNumber = 0;
	private Deque<Byte> receiveBuffer = null;
	private List<Byte> lastTransportSegment = new ArrayList<>();
	
	private TransportSegmentEncoder encoder = new TransportSegmentEncoderImpl();
	private TransportSegmentDecoder decoder = new TransportSegmentDecoderImpl();

	public synchronized void sendData(List<Byte> data) {
		if (dataLinkLayer == null) {
			throw new IllegalStateException("Cannot transmit data.  No DataLinkLayer has been defined.");
		}
		
		int parts = data.size() / (maximumTransmissionUnit - 1);
		if (data.size() % (maximumTransmissionUnit - 1) != 0) {
			++parts;
		}
		
		int sequenceNumber = -1;
		for (int i = 0; i < parts; ++i) {
			TransportSegment transportSegment = new TransportSegment();
			transportSegment.getTransportHeader().setFirstSegment(i == 0);
			transportSegment.getTransportHeader().setFinalSegment(i == (parts - 1));
			transportSegment.getTransportHeader().setSequenceNumber(++sequenceNumber);
			transportSegment.setData(new ArrayList<Byte>(data.subList(i * (maximumTransmissionUnit - 1), ((i + 1) * (maximumTransmissionUnit - 1) > data.size()) ? data.size() : ((i + 1) * (maximumTransmissionUnit - 1)))));
			sequenceNumber %= 64;
			
			dataLinkLayer.sendData(encoder.encode(transportSegment));
		}
	}

	public void receiveData(List<Byte> data) {
		if (data.equals(lastTransportSegment)) {
			return;
		}

		TransportSegment transportSegment = decoder.decode(data);
		if (transportSegment.getTransportHeader().isFirstSegment()) {
			receiveBuffer = new ArrayDeque<>();
			receiveBuffer.addAll(transportSegment.getData());
			lastSequenceNumber = transportSegment.getTransportHeader().getSequenceNumber();
			lastTransportSegment = new ArrayList<>(data);
			
			if (transportSegment.getTransportHeader().isFinalSegment()) {
				applicationLayer.dataReceived(new ArrayList<Byte>(receiveBuffer));
				receiveBuffer = new ArrayDeque<Byte>();
			}
		} else if (receiveBuffer != null) {
			if ((lastSequenceNumber + 1)%64 == transportSegment.getTransportHeader().getSequenceNumber()) {
				receiveBuffer.addAll(transportSegment.getData());
				lastSequenceNumber = transportSegment.getTransportHeader().getSequenceNumber();
				lastTransportSegment = new ArrayList<>(data);
				
				if (transportSegment.getTransportHeader().isFinalSegment()) {
					applicationLayer.dataReceived(new ArrayList<Byte>(receiveBuffer));
					receiveBuffer = new ArrayDeque<Byte>();
				}
			}
		}
	}

	public int getMaximumTransmissionUnit() {
		return maximumTransmissionUnit;
	}

	public void setMaximumTransmissionUnit(int maximumTransmissionUnit) {
		if (maximumTransmissionUnit < 2) {
			maximumTransmissionUnit = 2;
		}
		this.maximumTransmissionUnit = maximumTransmissionUnit;
	}

	public int getMaximumReceiveDataSize() {
		return maximumReceiveDataSize;
	}

	public void setMaximumReceiveDataSize(int maximumReceiveDataSize) {
		this.maximumReceiveDataSize = maximumReceiveDataSize;
	}

	public void setDataLinkLater(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
	}

	public void setApplicationLayer(ApplicationLayer applicationLayer) {
		this.applicationLayer = applicationLayer;
	}
}
