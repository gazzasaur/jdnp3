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
package net.sf.jdnp3.dnp3.stack.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

public class DataPump {
	private Thread thread = null;
	private DataPumpWorker dataPumpWorker = new DataPumpWorker();
	
	public void registerServerChannel(SelectableChannel selectableChannel, DataPumpListener dataPumpListener) {
		dataPumpWorker.registerServerChannel(selectableChannel, dataPumpListener);
	}
	
	public void registerChannel(SocketChannel socketChannel, DataPumpListener dataListener) {
		dataPumpWorker.registerChannel(socketChannel, dataListener);
	}
	
	public void sendData(SocketChannel socketChannel, List<Byte> data) {
		dataPumpWorker.sendData(socketChannel, data);
	}
	
	public void start() {
		if (thread == null || !thread.isAlive()) {
			thread = new Thread(dataPumpWorker);
			thread.start();
		}
	}
}
