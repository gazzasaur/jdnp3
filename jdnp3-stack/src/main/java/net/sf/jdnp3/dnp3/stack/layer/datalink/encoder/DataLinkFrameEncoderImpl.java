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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameEncoderImpl implements DataLinkFrameEncoder {
	private DataLinkFrameHeaderEncoder dataLinkFrameHeaderEncoder = new DataLinkFrameHeaderEncoder();
	
	public List<Byte> encode(DataLinkFrame dataLinkFrame) {
		List<Byte> partBuffer = new ArrayList<>();
		for (int i = 0; i < dataLinkFrame.getData().size(); i += 16) {
			List<Byte> buffer = dataLinkFrame.getData().subList(i, (i + 16 > dataLinkFrame.getData().size()) ? dataLinkFrame.getData().size() : (i + 16));
			partBuffer.addAll(buffer);
			DataUtils.addInteger16(Crc16.computeCrc(buffer), partBuffer);
			System.out.println(Integer.toHexString(Crc16.computeCrc(buffer)));
		}
		
		List<Byte> data = new ArrayList<>();
		dataLinkFrame.getDataLinkFrameHeader().setLength(partBuffer.size() + 3);
		dataLinkFrameHeaderEncoder.encode(dataLinkFrame.getDataLinkFrameHeader(), data);
		data.addAll(partBuffer);

		return data;
	}
}
