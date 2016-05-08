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
package net.sf.jdnp3.dnp3.stack.layer.datalink.decoder;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkConstants.DNP3_START_BYTES;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameHeaderDecoder {
	private static final int LENGTH_OFFSET = 2;
	private static final int SOURCE_OFFSET = 6;
	private static final int CONTROL_OFFSET = 3;
	private static final int DESTINATION_OFFSET = 4;
	private static final int MINIMUM_HEADER_SIZE = 10;

	public void decode(DataLinkFrameHeader dataLinkFrameHeader, List<Byte> data) {
		if (data.size() < MINIMUM_HEADER_SIZE) {
			throw new IllegalArgumentException("Cannot parse the data as the header is too small.");
		}
		if (DataUtils.getInteger(0, 2, data) != DNP3_START_BYTES) {
			throw new IllegalArgumentException("Start byte were not detected.");
		}
		dataLinkFrameHeader.setLength((int) DataUtils.getInteger(LENGTH_OFFSET, 1, data));
		BitSet bitSet = BitSet.valueOf(new byte[] { data.get(CONTROL_OFFSET) });
		dataLinkFrameHeader.setPrimary((bitSet.get(7)) ? true : false);
		dataLinkFrameHeader.setDirection((bitSet.get(6)) ? Direction.MASTER_TO_OUTSTATION : Direction.OUTSTATION_TO_MASTER);
		
		boolean foundFunctionCode = false;
		int functionCodeValue = data.get(CONTROL_OFFSET) & 0x0F;
		for (FunctionCode functionCode : FunctionCode.values()) {
			if (functionCode.getCode() == functionCodeValue) {
				dataLinkFrameHeader.setFunctionCode(functionCode);
				foundFunctionCode = true;
			}
		}
		if (!foundFunctionCode) {
			throw new IllegalArgumentException("No function code matches: " + functionCodeValue);
		}
		
		dataLinkFrameHeader.setDestination((int) DataUtils.getInteger(DESTINATION_OFFSET, 2, data));
		dataLinkFrameHeader.setSource((int) DataUtils.getInteger(SOURCE_OFFSET, 2, data));
		
		dataLinkFrameHeader.setCheckSum((int) DataUtils.getInteger(8, 2, data));
		int calculatedCheckSum = Crc16.computeCrc(data.subList(0, 8));
		if (calculatedCheckSum != dataLinkFrameHeader.getCheckSum()) {
			throw new IllegalStateException("Invalid checksum.");
		}
	}
}
