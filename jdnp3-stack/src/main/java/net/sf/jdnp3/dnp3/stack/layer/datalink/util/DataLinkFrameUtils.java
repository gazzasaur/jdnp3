/**
 * Copyright 2016 Graeme Farquharson
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
 * 
 * Note:
 * I am not sure how fair it is to license this under this project.
 * This was taken from IEEE Std 1815 (2012) and adapted to Java.
 * If you are responsible for this work and feel this is unfair
 * please let me know and I will apply what ever recognition
 * you would like to this file.
 */
package net.sf.jdnp3.dnp3.stack.layer.datalink.util;

import static java.lang.String.format;

import java.util.BitSet;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameUtils {
	public static int headerLengthToRawLength(int headerLength) {
		return 2*((headerLength - 5)/16) + (((headerLength - 5)%16 > 0) ? 2 : 0) + headerLength + 5;
	}
	
	public static byte computeControlField(DataLinkFrameHeader dataLinkFrameHeader) {
		BitSet controlField = new BitSet(8);
		controlField.set(7, dataLinkFrameHeader.getDirection().equals(Direction.MASTER_TO_OUTSTATION));
		controlField.set(6, dataLinkFrameHeader.isPrimary());
		controlField.set(5, dataLinkFrameHeader.isFcb());
		controlField.set(4, dataLinkFrameHeader.isFcvDfc());
		byte controlFieldValue = DataUtils.bitSetToByte(controlField);
		controlFieldValue |= dataLinkFrameHeader.getFunctionCode().getCode();
		return controlFieldValue;
	}
	
	public static String toString(ChannelId channelId, DataLinkFrame dataLinkFrame) {
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append(format("Channel Id    : %s\n", channelId));
		stringBuilder.append(format("Function Code : %s\n", dataLinkFrame.getDataLinkFrameHeader().getFunctionCode()));
		stringBuilder.append(format("Length        : %s\n", dataLinkFrame.getDataLinkFrameHeader().getLength()));
		stringBuilder.append(format("Source        : %s\n", dataLinkFrame.getDataLinkFrameHeader().getSource()));
		stringBuilder.append(format("Destination   : %s\n", dataLinkFrame.getDataLinkFrameHeader().getDestination()));
		stringBuilder.append(format("Primary       : %s\n", dataLinkFrame.getDataLinkFrameHeader().isPrimary()));
		stringBuilder.append(format("Direction     : %s\n", dataLinkFrame.getDataLinkFrameHeader().getDirection()));
		stringBuilder.append(format("FCB           : %s\n", dataLinkFrame.getDataLinkFrameHeader().isFcb()));
		stringBuilder.append(format("FCV/DFC       : %s\n", dataLinkFrame.getDataLinkFrameHeader().isFcvDfc()));
		stringBuilder.append(format("Data          : %s", DataUtils.toString(dataLinkFrame.getData())));
		return stringBuilder.toString();
	}
}
