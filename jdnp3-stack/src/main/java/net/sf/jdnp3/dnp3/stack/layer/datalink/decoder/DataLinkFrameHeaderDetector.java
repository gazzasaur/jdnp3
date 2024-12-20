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

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkConstants.DNP3_START_BYTE;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkConstants.DNP3_START_BYTES;

import java.util.ArrayDeque;
import java.util.BitSet;
import java.util.Deque;
import java.util.Iterator;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameHeaderDetector {
	private static final int LENGTH_OFFSET = 2;
	private static final int SOURCE_OFFSET = 6;
	private static final int CONTROL_OFFSET = 3;
	private static final int DESTINATION_OFFSET = 4;
	private static final int MINIMUM_HEADER_SIZE = 10;

	public boolean detectHeader(DataLinkFrameHeader dataLinkFrameHeader, Deque<Byte> data) {
		if (data.size() >= 2 && DataUtils.getUnsignedInteger(0, 2, data) != DNP3_START_BYTES) {
			throw new IllegalArgumentException("Start bytes were not detected.");
		} else if (data.size() == 1 && DataUtils.getUnsignedInteger(0, 1, data) != DNP3_START_BYTE) {
			throw new IllegalArgumentException("Start byte was not detected.");
		}
		if (data.size() < MINIMUM_HEADER_SIZE) {
			return false;
		}
		
		dataLinkFrameHeader.setLength((int) DataUtils.getUnsignedInteger(LENGTH_OFFSET, 1, data));
		byte controlByte = DataUtils.seek(data, CONTROL_OFFSET).next();
		BitSet bitSet = BitSet.valueOf(new byte[] { controlByte });
		dataLinkFrameHeader.setPrimary((bitSet.get(7)) ? true : false);
		dataLinkFrameHeader.setDirection((bitSet.get(6)) ? Direction.MASTER_TO_OUTSTATION : Direction.OUTSTATION_TO_MASTER);
		
		boolean foundFunctionCode = false;
		int functionCodeValue = controlByte & 0x0F;
		for (FunctionCode functionCode : FunctionCode.values()) {
			if (functionCode.getCode() == functionCodeValue) {
				dataLinkFrameHeader.setFunctionCode(functionCode);
				foundFunctionCode = true;
			}
		}
		if (!foundFunctionCode) {
			throw new IllegalArgumentException(format("No function code matches: %02X", functionCodeValue));
		}
		
		dataLinkFrameHeader.setDestination((int) DataUtils.getUnsignedInteger(DESTINATION_OFFSET, 2, data));
		dataLinkFrameHeader.setSource((int) DataUtils.getUnsignedInteger(SOURCE_OFFSET, 2, data));
		
		Deque<Byte> crcChunk = new ArrayDeque<>();
		Iterator<Byte> it = data.iterator();
		for (int i = 0; i < 8; ++i) {
			crcChunk.offerLast(it.next());
		}
		
		dataLinkFrameHeader.setCheckSum((int) DataUtils.getUnsignedInteger(8, 2, data));
		int calculatedCheckSum = Crc16.computeCrc(crcChunk);
		if (calculatedCheckSum != dataLinkFrameHeader.getCheckSum()) {
			throw new IllegalStateException(format("Invalid checksum.  Expected %02X but got %02X.", calculatedCheckSum, dataLinkFrameHeader.getCheckSum()));
		}
		return true;
	}
}
