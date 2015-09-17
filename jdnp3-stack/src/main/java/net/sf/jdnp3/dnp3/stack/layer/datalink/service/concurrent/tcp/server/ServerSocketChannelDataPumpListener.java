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
package net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpListener;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpWorker;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerSocketChannelDataPumpListener implements DataPumpListener {
	private Logger logger = LoggerFactory.getLogger(ServerSocketChannelDataPumpListener.class);
	
	private DataPumpWorker dataPump;
	private ChannelManager channelManager;
	private ExecutorService executorService;
	private DataLinkListener dataLinkListener;
	private ServerSocketChannel serverSocketChannel;

	public ServerSocketChannelDataPumpListener(DataPumpWorker dataPump, ExecutorService executorService, ChannelManager channelManager, ServerSocketChannel serverSocketChannel, DataLinkListener dataLinkListener) {
		this.dataPump = dataPump;
		this.channelManager = channelManager;
		this.executorService = executorService;
		this.dataLinkListener = dataLinkListener;
		this.serverSocketChannel = serverSocketChannel;
	}
	
	public void connected() {
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			
			ChannelId channelId = channelManager.addChannel(socketChannel);
			dataPump.registerChannel(socketChannel, new SocketChannelDataPumpListener(channelId, executorService, channelManager, dataLinkListener));
		} catch (Exception e) {
			logger.error("Failed to accept client socket.", e);
			try {
				if (socketChannel != null) {
					socketChannel.close();
				}
			} catch (Exception sce) {
				logger.error("Failed to close socket channel.", sce);
			}
		}
	}

	public void disconnected() {
		try {
			serverSocketChannel.close();
		} catch (Exception e) {
			logger.error("FAiled to close ServerSocketChannel.", e);
		}
		channelManager.closeAll();
	}

	public void dataReceived(List<Byte> data) {
	}
}
