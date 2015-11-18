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

import static java.lang.String.format;

import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class TransportSegmentUtils {
	public static String toString(ChannelId channelId, TransportSegment transportSegment) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Transport Segment\n");
		stringBuilder.append(format("Channel Id      : %s\n", channelId));
		stringBuilder.append(format("Sequence Number : %s\n", transportSegment.getTransportHeader().getSequenceNumber()));
		stringBuilder.append(format("First Segment   : %s\n", transportSegment.getTransportHeader().isFirstSegment()));
		stringBuilder.append(format("Final Segment   : %s\n", transportSegment.getTransportHeader().isFinalSegment()));
		stringBuilder.append(format("Data            : %s", DataUtils.toString(transportSegment.getData())));
		return stringBuilder.toString();
	}
}
