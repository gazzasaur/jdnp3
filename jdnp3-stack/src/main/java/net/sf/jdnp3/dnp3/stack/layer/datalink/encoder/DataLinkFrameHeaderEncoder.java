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
 */
package net.sf.jdnp3.dnp3.stack.layer.datalink.encoder;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16.computeCrc;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkConstants.DNP3_START_BYTES;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils.computeControlField;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;

public class DataLinkFrameHeaderEncoder {
	public static void encode(DataLinkFrameHeader dataLinkFrameHeader, List<Byte> data) {
		List<Byte> buffer = new ArrayList<>();
		
		addInteger(DNP3_START_BYTES, 2, buffer);
		addInteger(dataLinkFrameHeader.getLength(), 1, buffer);
		addInteger(computeControlField(dataLinkFrameHeader), 1, buffer);
		addInteger(dataLinkFrameHeader.getDestination(), 2, buffer);
		addInteger(dataLinkFrameHeader.getSource(), 2, buffer);
		
		data.addAll(buffer);
		addInteger(computeCrc(buffer), 2, data);
	}
}
