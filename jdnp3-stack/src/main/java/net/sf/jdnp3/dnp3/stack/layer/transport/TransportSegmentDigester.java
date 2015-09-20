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

import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TransportSegmentDigester {
	private Logger logger = LoggerFactory.getLogger(TransportSegmentDigester.class);
	
	private int applicationMtu = 0;
	private int lastSequenceNumber = 0;
	private Deque<Byte> receiveBuffer = null;
	private List<Byte> applicationData = null;
	private List<Byte> lastTransportSegment = new ArrayList<>();
	

	public boolean digestData(MessageProperties messageProperties, TransportSegment transportSegment, List<Byte> data) {
		if (data.equals(lastTransportSegment)) {
			logger.warn("Duplicate packet received from " + messageProperties.getSourceAddress());
			return false;
		}

		if (transportSegment.getTransportHeader().isFirstSegment()) {
			receiveBuffer = new ArrayDeque<>();
			receiveBuffer.addAll(transportSegment.getData());
			lastSequenceNumber = transportSegment.getTransportHeader().getSequenceNumber();
			lastTransportSegment = new ArrayList<>(data);
			
			if (receiveBuffer.size() > applicationMtu) {
				int receivedSize = receiveBuffer.size();
				receiveBuffer.clear();
				logger.warn(String.format("Received %s bytes but the MTU onlypermits %s.", receivedSize, applicationMtu));
			} else if (transportSegment.getTransportHeader().isFinalSegment()) {
				applicationData = new ArrayList<Byte>(receiveBuffer);
				receiveBuffer = new ArrayDeque<Byte>();
				return true;
			}
		} else if (receiveBuffer != null) {
			if ((lastSequenceNumber + 1)%64 == transportSegment.getTransportHeader().getSequenceNumber()) {
				receiveBuffer.addAll(transportSegment.getData());
				lastSequenceNumber = transportSegment.getTransportHeader().getSequenceNumber();
				lastTransportSegment = new ArrayList<>(data);
				
				if (receiveBuffer.size() > applicationMtu) {
					int receivedSize = receiveBuffer.size();
					receiveBuffer.clear();
					logger.warn(String.format("Received %s bytes but the MTU onlypermits %s.", receivedSize, applicationMtu));
				} else if (transportSegment.getTransportHeader().isFinalSegment()) {
					applicationData = new ArrayList<Byte>(receiveBuffer);
					receiveBuffer = new ArrayDeque<Byte>();
					return true;
				}
			}
		}
		return false;
	}

	public List<Byte> pollData() {
		if (applicationData == null) {
			throw new IllegalArgumentException("Application data is not ready.");
		}
		List<Byte> returnValue = applicationData;
		applicationData = null;
		return returnValue;
	}
	
	public void setApplicationMtu(int applicationMtu) {
		this.applicationMtu = applicationMtu;
	}
}
