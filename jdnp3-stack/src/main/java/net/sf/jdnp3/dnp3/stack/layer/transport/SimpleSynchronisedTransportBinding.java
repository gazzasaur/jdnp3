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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class SimpleSynchronisedTransportBinding implements TransportBinding {
	private Logger logger = LoggerFactory.getLogger(SimpleSynchronisedTransportBinding.class);
	
	private int address;
	private DataLinkLayer dataLinkLayer;
	private ApplicationLayer applicationLayer;
	private TransportSegmentEncoder transportSegmentEncoder = new TransportSegmentEncoderImpl();
	private TransportSegmentDecoder transportSegmentDecoder = new TransportSegmentDecoderImpl();
	private TransportSegmentDigester transportSegmentDigester = new TransportSegmentDigester();
	private TransportSegmentSplitter transportSegmentSplitter = new TransportSegmentSplitter();

	public synchronized void receiveDataLinkData(MessageProperties messageProperties, List<Byte> data) {
		validate();
		if (address != messageProperties.getDestinationAddress()) {
			return;
		}
		TransportSegment transportSegment = transportSegmentDecoder.decode(data);
		logger.debug(TransportSegmentUtils.toString(messageProperties.getChannelId(), transportSegment));
		if (transportSegmentDigester.digestData(messageProperties, transportSegment, data)) {
			applicationLayer.dataReceived(messageProperties, transportSegmentDigester.pollData());
		}
	}

	public synchronized void receiveApplicationData(MessageProperties messageProperties, List<Byte> data) {
		validate();
		List<TransportSegment> transportSegments = transportSegmentSplitter.splitData(messageProperties, data);
		for (TransportSegment transportSegment : transportSegments) {
			dataLinkLayer.sendData(messageProperties, transportSegmentEncoder.encode(transportSegment));
		}
	}
	
	public synchronized void setApplicationLayer(int address, ApplicationLayer applicationLayer) {
		this.address = address;
		this.applicationLayer = applicationLayer;
		transportSegmentDigester.setApplicationMtu(applicationLayer.getMtu());
	}
	
	public synchronized void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
		transportSegmentSplitter.setDataLinkMtu(dataLinkLayer.getMtu());
	}

	private void validate() {
		if (dataLinkLayer == null) {
			throw new IllegalStateException("No DataLinkLayer has been defined in the binding.");
		}
		if (applicationLayer == null) {
			throw new IllegalStateException("No ApplicationLayer has been defined in the binding.");
		}
	}
}
