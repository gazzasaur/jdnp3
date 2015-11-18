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

import java.nio.channels.SocketChannel;

public class SocketChannelUtils {
	public static String getLocalSocketAddress(SocketChannel socketChannel) {
		try {
			return socketChannel.getLocalAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Invalid";
		}
	}

	public static String getRemoteSocketAddress(SocketChannel socketChannel) {
		try {
			return socketChannel.getRemoteAddress().toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "Invalid";
		}
	}
}
