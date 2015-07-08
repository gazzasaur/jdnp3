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

public class TransportSegmentEncoderImpl implements TransportSegmentEncoder {
	public List<Byte> encode(TransportSegment transportSegment) {
		List<Byte> data = new ArrayList<>();
		BitSet flags = new BitSet();
		flags.set(7, transportSegment.getTransportHeader().isFinalSegment());
		flags.set(6, transportSegment.getTransportHeader().isFirstSegment());
		byte[] rawFlags = flags.toByteArray();
		byte value = (rawFlags.length > 0) ? rawFlags[0] : 0;
		value |= transportSegment.getTransportHeader().getSequenceNumber();
		data.add(value);
		data.addAll(transportSegment.getData());
		return data;
	}
}
