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
import java.util.List;

import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public class TransportSegmentSplitter {
	private int dataLinkMtu = 0;

	public List<TransportSegment> splitData(MessageProperties messageProperties, List<Byte> data) {
		if (dataLinkMtu <= 0) {
			throw new IllegalStateException("The DataLink MTU must be greater than 0 but was " + dataLinkMtu);
		}
		
		int parts = data.size() / (dataLinkMtu - 1);
		if (data.size() % (dataLinkMtu - 1) != 0) {
			++parts;
		}
		List<TransportSegment> transportSegments = new ArrayList<>();
		
		int sequenceNumber = -1;
		for (int i = 0; i < parts; ++i) {
			TransportSegment transportSegment = new TransportSegment();
			transportSegment.getTransportHeader().setFirstSegment(i == 0);
			transportSegment.getTransportHeader().setFinalSegment(i == (parts - 1));
			transportSegment.getTransportHeader().setSequenceNumber(++sequenceNumber);
			transportSegment.setData(new ArrayList<Byte>(data.subList(i * (dataLinkMtu - 1), ((i + 1) * (dataLinkMtu - 1) > data.size()) ? data.size() : ((i + 1) * (dataLinkMtu - 1)))));
			sequenceNumber %= 64;
			
			transportSegments.add(transportSegment);
		}
		return transportSegments;
	}

	public void setDataLinkMtu(int dataLinkMtu) {
		this.dataLinkMtu = dataLinkMtu;
	}
	
}
