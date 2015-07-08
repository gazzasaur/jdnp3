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
package net.sf.jdnp3.dnp3.stack.layer.datalink.encoder;

import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger16;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;

public class DataLinkFrameHeaderEncoder {
	public void encode(DataLinkFrameHeader dataLinkFrameHeader, List<Byte> data) {
		List<Byte> buffer = new ArrayList<>();
		addInteger16(dataLinkFrameHeader.getStart(), buffer);
		buffer.add((byte) dataLinkFrameHeader.getLength());
		
		BitSet controlField = new BitSet(8);
		controlField.set(7, dataLinkFrameHeader.getDirection().equals(Direction.MASTER_TO_OUTSTATION));
		controlField.set(6);
		byte controlFieldValue = controlField.toByteArray()[0];
		controlFieldValue |= dataLinkFrameHeader.getFunctionCode().getCode();
		buffer.add(controlFieldValue);
		
		addInteger16(dataLinkFrameHeader.getDestination(), buffer);
		addInteger16(dataLinkFrameHeader.getSource(), buffer);
		
		data.addAll(buffer);
		addInteger16(Crc16.computeCrc(buffer), data);
	}
}
