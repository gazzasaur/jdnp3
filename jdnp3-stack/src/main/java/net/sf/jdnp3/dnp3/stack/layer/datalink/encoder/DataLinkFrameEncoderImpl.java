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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class DataLinkFrameEncoderImpl implements DataLinkFrameEncoder {
	public Deque<Byte> encode(DataLinkFrame dataLinkFrame) {
		int partCount = 0;
		Deque<Byte> partBuffer = new ArrayDeque<>();
		List<Byte> dataLinkFrameData = new ArrayList<>(dataLinkFrame.getData());
		for (int i = 0; i < dataLinkFrameData.size(); i += 16) {
			Deque<Byte> buffer = new ArrayDeque<>(dataLinkFrameData.subList(i, (i + 16 > dataLinkFrameData.size()) ? dataLinkFrameData.size() : (i + 16)));
			partBuffer.addAll(buffer);
			partCount += buffer.size();
			DataUtils.addInteger(Crc16.computeCrc(buffer), 2, partBuffer);
		}
		
		Deque<Byte> data = new ArrayDeque<>();
		dataLinkFrame.getDataLinkFrameHeader().setLength(partCount + 5);
		DataLinkFrameHeaderEncoder.encode(dataLinkFrame.getDataLinkFrameHeader(), data);
		data.addAll(partBuffer);

		return data;
	}
}
