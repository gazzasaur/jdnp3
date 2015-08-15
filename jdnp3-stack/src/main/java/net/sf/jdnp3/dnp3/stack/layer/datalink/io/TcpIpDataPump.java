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
package net.sf.jdnp3.dnp3.stack.layer.datalink.io;

import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class TcpIpDataPump {
	private TcpIpDataSinkWorker tcpIpDataSinkWorker = new TcpIpDataSinkWorker();
	private TcpIpDataSourceWorker tcpIpDataSourceWorker = new TcpIpDataSourceWorker();
	
	public TcpIpDataPump() {
		new Thread(tcpIpDataSinkWorker).start();
		new Thread(tcpIpDataSourceWorker).start();
	}
	
	public void sendData(SocketChannel socketChannel, List<Byte> data) {
		tcpIpDataSinkWorker.sendData(socketChannel, data);
	}

	public void registerSource(SocketChannel socketChannel, ConcurrentLinkedDeque<Byte> dataBuffer) {
		tcpIpDataSourceWorker.registerSource(socketChannel, dataBuffer);
	}
}
