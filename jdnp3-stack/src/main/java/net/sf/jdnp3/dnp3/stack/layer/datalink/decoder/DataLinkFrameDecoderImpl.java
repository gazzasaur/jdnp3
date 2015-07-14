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
package net.sf.jdnp3.dnp3.stack.layer.datalink.decoder;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameDecoderImpl implements DataLinkFrameDecoder {
	private DataLinkFrameHeaderDecoder dataLinkFrameHeaderDecoder = new DataLinkFrameHeaderDecoder();
	
	public DataLinkFrame decode(List<Byte> data) {
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrameHeaderDecoder.decode(dataLinkFrame.getDataLinkFrameHeader(), data);
		
		int chunks = (dataLinkFrame.getDataLinkFrameHeader().getRawLength() - 10)/18 + (((dataLinkFrame.getDataLinkFrameHeader().getRawLength() - 10)%18 > 0) ? 1 : 0);
		for (int i = 0; i < chunks; ++i) {
			int crcOffset = Math.min(i * 18 + 26, dataLinkFrame.getDataLinkFrameHeader().getRawLength() - 2);
			List<Byte> chunk = new ArrayList<>();
			for (int j = i * 18 + 10; j < crcOffset; ++j) {
				chunk.add(data.get(j));
			}
			
			int expectedCheckSum = Crc16.computeCrc(chunk);
			int actualCheckSum = (int) DataUtils.getInteger16(crcOffset, data);
			if (expectedCheckSum != actualCheckSum) {
				for (Byte chunkByte : chunk) {
					System.out.print(format("%02X", chunkByte));
				}
				System.out.println();
				throw new IllegalStateException(String.format("Mismatched Crc on chunk %d.  Expected %d but got %d.", i, expectedCheckSum, actualCheckSum));
			}
			
			dataLinkFrame.getData().addAll(chunk);
		}
		
		return dataLinkFrame;
	}
}
