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
package net.sf.jdnp3.dnp3.stack.nio;

public class DataPumpItem {
	private int maxBufferSize;
	private DataPumpListener dataPumpListener;
	private DataPumpSendBuffer dataPumpSendBuffer;
	private DataPumpTransceiver dataPumpTransceiver;
	
	public DataPumpItem(int maxBufferSize, DataPumpTransceiver dataPumpTransceiver, DataPumpListener dataPumpListener) {
		this.maxBufferSize = maxBufferSize;
		this.dataPumpListener = dataPumpListener;
		this.dataPumpTransceiver = dataPumpTransceiver;
		dataPumpSendBuffer = new DataPumpSendBuffer(maxBufferSize);
	}

	public int getMaxBufferSize() {
		return maxBufferSize;
	}
	
	public DataPumpListener getDataPumpListener() {
		return dataPumpListener;
	}

	public DataPumpSendBuffer getDataPumpSendBuffer() {
		return dataPumpSendBuffer;
	}

	public DataPumpTransceiver getDataPumpTransceiver() {
		return dataPumpTransceiver;
	}
}
