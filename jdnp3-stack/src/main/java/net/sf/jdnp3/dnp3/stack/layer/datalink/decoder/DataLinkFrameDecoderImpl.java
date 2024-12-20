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

import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils.headerLengthToRawLength;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameDecoderImpl implements DataLinkFrameDecoder {
	private DataLinkFrameHeaderDecoder dataLinkFrameHeaderDecoder = new DataLinkFrameHeaderDecoder();
	
	public DataLinkFrame decode(Deque<Byte> data) {
		Deque<Byte> dataChunks = new ArrayDeque<Byte>(data.size());
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrameHeaderDecoder.decode(dataLinkFrame.getDataLinkFrameHeader(), data);

		Deque<Byte> crcChunk = new ArrayDeque<>();
		Iterator<Byte> it = DataUtils.seek(data, 10);
		int chunks = (headerLengthToRawLength(dataLinkFrame.getDataLinkFrameHeader().getLength()) - 10)/18 + (((headerLengthToRawLength(dataLinkFrame.getDataLinkFrameHeader().getLength()) - 10)%18 > 0) ? 1 : 0);
		for (int i = 0; i < chunks; ++i) {
			int crcOffset = Math.min(i * 18 + 26, headerLengthToRawLength(dataLinkFrame.getDataLinkFrameHeader().getLength()) - 2);
			Deque<Byte> chunk = new ArrayDeque<>();
			for (int j = i * 18 + 10; j < crcOffset; ++j) {
				chunk.add(it.next());
			}
			
			crcChunk.offerLast(it.next());
			crcChunk.offerLast(it.next());
			int expectedCheckSum = Crc16.computeCrc(chunk);
			int actualCheckSum = (int) DataUtils.getUnsignedInteger(0, 2, crcChunk);
			if (expectedCheckSum != actualCheckSum) {
				throw new IllegalStateException(String.format("Mismatched Crc on chunk %d.  Expected %d but got %d.", i, expectedCheckSum, actualCheckSum));
			}
			
			dataChunks.addAll(chunk);
			crcChunk.pollFirst();
			crcChunk.pollFirst();
		}
		dataLinkFrame.setData(dataChunks);
		
		return dataLinkFrame;
	}
}
