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
package net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.SocketChannelUtils.getRemoteSocketAddress;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpListener;

public class ServerSocketChannelDataPumpListener implements DataPumpListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(ServerSocketChannelDataPumpListener.class);
	
	private DataPump dataPump;
	private ChannelManager channelManager;
	private DataLinkInterceptor dataLinkInterceptor;
	private ServerSocketChannel serverSocketChannel;

	public ServerSocketChannelDataPumpListener(DataPump dataPump, ChannelManager channelManager, ServerSocketChannel serverSocketChannel, DataLinkInterceptor dataLinkInterceptor) {
		this.dataPump = dataPump;
		this.channelManager = channelManager;
		this.dataLinkInterceptor = dataLinkInterceptor;
		this.serverSocketChannel = serverSocketChannel;
	}
	
	public void connected() {
		SocketChannel socketChannel = null;
		try {
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			
			ChannelId channelId = channelManager.addChannel(socketChannel);
			LOGGER.info("Connection received from {} and has been assigned a channel id of {}.", getRemoteSocketAddress(socketChannel), channelId);
			dataPump.registerAcceptedChannel(socketChannel, new SocketChannelDataPumpListener(channelId, channelManager, dataLinkInterceptor));
		} catch (Exception e) {
			LOGGER.error("Failed to accept client socket.", e);
			try {
				if (socketChannel != null) {
					socketChannel.close();
				}
			} catch (Exception sce) {
				LOGGER.error("Failed to close socket channel.", sce);
			}
		}
	}

	public void disconnected() {
		try {
			serverSocketChannel.close();
		} catch (Exception e) {
			LOGGER.error("FAiled to close ServerSocketChannel.", e);
		}
		channelManager.closeAll();
	}

	public void dataReceived(List<Byte> data) {
	}
}
