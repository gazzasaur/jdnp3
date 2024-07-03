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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;

public class DataLinkDigester {
	private static final int CHARACTER_TIMEOUT = 500;
	private Logger logger = LoggerFactory.getLogger(DataLinkDigester.class);
	
	private int mtu = 253;
	
	private long lastDrop;
	private int maximumReceiveDataSize = 65535;
	private List<Byte> frameBuffer = new ArrayList<>();
	private DataLinkFrameDecoder decoder = new DataLinkFrameDecoderImpl();
	private DataLinkFrameHeaderDetector detector = new DataLinkFrameHeaderDetector();
	
	private DataLinkFrame dataLinkFrame = null;
	
	public boolean digest(List<Byte> data) {
		if (frameBuffer.isEmpty()) {
			lastDrop = new Date().getTime();
		}
		frameBuffer.addAll(data);
		
		try {
			DataLinkFrameHeader dataLinkFrameHeader = new DataLinkFrameHeader();
			if (detector.detectHeader(dataLinkFrameHeader, frameBuffer) && headerLengthToRawLength(dataLinkFrameHeader.getLength()) <= frameBuffer.size()) {
				this.dataLinkFrame = decoder.decode(frameBuffer);
				frameBuffer = new ArrayList<>(frameBuffer.subList(headerLengthToRawLength(dataLinkFrameHeader.getLength()), frameBuffer.size()));
				lastDrop = new Date().getTime();
				return true;
			}
		} catch (Exception e) {
			logger.warn("Error found on stream.", e);
			performRapidDrop(frameBuffer);
		}
		
		if (frameBuffer.size() == 0) {
			lastDrop = new Date().getTime();
		}
		if (new Date().getTime() - lastDrop > CHARACTER_TIMEOUT) {
			logger.warn("Timeout on stream.");
			performRapidDrop(frameBuffer);
		}
		if (frameBuffer.size() > maximumReceiveDataSize) {
			logger.warn("Receive buffer has exceeded max size.");
			performRapidDrop(frameBuffer);
		}
		return false;
	}

	public DataLinkFrame getDataLinkFrame() {
		DataLinkFrame returnFrame = dataLinkFrame;
		dataLinkFrame = null;
		if (returnFrame == null) {
			throw new IllegalStateException("No DataLinkFrame is ready yet.");
		}
		return returnFrame;
	}

	private void performRapidDrop(List<Byte> data) {
		if (data.size() < 1) {
			return;
		}
		List<Byte> droppedData = new ArrayList<>();
		droppedData.add(data.remove(0));
		for (int i = 0; i < mtu; ++i) {
			try {
				DataLinkFrameHeader dataLinkFrameHeader = new DataLinkFrameHeader();
				detector.detectHeader(dataLinkFrameHeader, data);
				break;
			} catch (Exception e) {
				droppedData.add(data.remove(0));
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Dropping: ");
		for (Byte droppedByte : droppedData) {
			stringBuilder.append(String.format("%02X", droppedByte));
		}
		logger.warn(stringBuilder.toString());
		lastDrop = new Date().getTime();
	}
}
