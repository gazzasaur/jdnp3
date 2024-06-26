/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.client;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpClientDataLinkServiceConnector {
	private static final Logger logger = LoggerFactory.getLogger(TcpClientDataLinkServiceConnector.class);
	
	public static SocketChannel create(String host, int port) {
		SocketChannel socketChannel = null;
		try {
			socketChannel = SocketChannel.open();
			socketChannel.configureBlocking(false);
			socketChannel.connect(new InetSocketAddress(host, port));
		} catch (Exception e) {
			closeChannel(socketChannel);
			throw new RuntimeException(e);
		}
		return socketChannel;
	}

	public static void closeChannel(SocketChannel channel) {
		try {
			channel.close();
		} catch (Exception e) {
			logger.error("Cannot close channel.", e);
		}
	}
}
