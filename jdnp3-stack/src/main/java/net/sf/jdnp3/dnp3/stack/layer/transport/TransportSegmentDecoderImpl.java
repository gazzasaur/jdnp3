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

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class TransportSegmentDecoderImpl implements TransportSegmentDecoder {
	private Logger logger = LoggerFactory.getLogger(TransportSegmentDecoderImpl.class);
	
	public TransportSegment decode(List<Byte> linkData) {
		logger.debug("Transport Data Received: " + DataUtils.toString(linkData));
		
		List<Byte> data = new ArrayList<Byte>(linkData);
		TransportSegment transportSegment = new TransportSegment();
		byte headerByte = data.remove(0);
		BitSet bitSet = BitSet.valueOf(new byte[] { headerByte });
		
		transportSegment.getTransportHeader().setFirstSegment(bitSet.get(7));
		transportSegment.getTransportHeader().setFinalSegment(bitSet.get(6));
		transportSegment.getTransportHeader().setSequenceNumber(headerByte & 0x3F);
		
		transportSegment.getData().addAll(data.subList(0, data.size()));
		
		logger.debug(TransportSegmentUtils.toString(transportSegment));
		return transportSegment;
	}
}
