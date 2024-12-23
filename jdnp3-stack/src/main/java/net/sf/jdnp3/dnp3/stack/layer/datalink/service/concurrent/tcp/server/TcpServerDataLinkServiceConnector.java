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

import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpServerDataLinkServiceConnector {
	private static final Logger logger = LoggerFactory.getLogger(TcpServerDataLinkServiceConnector.class);
	
	public static ServerSocketChannel create(String host, int port) {
		ServerSocketChannel serverSocketChannel = null;
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(host, port));
			serverSocketChannel.accept();
		} catch (Exception e) {
			closeChannel(serverSocketChannel);
			throw new RuntimeException(e);
		}
		return serverSocketChannel;
	}
	
	public static void closeChannel(ServerSocketChannel channel) {
		try {
			channel.close();
		} catch (Exception e) {
			logger.error("Cannot close channel.", e);
		}
	}
	
	public static void closeChannel(SocketChannel channel) {
		try {
			channel.close();
		} catch (Exception e) {
			logger.error("Cannot close channel.", e);
		}
	}
}
