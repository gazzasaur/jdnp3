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

import static net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.SocketChannelUtils.getLocalSocketAddress;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.SocketChannelUtils.getRemoteSocketAddress;

import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.message.BasicChannelId;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;

public class ChannelManager {
	private Logger logger = LoggerFactory.getLogger(ChannelManager.class);
	
	private Map<ChannelId, SocketChannel> connectedSocketChanels = new ConcurrentHashMap<>();
	
	public ChannelId addChannel(SocketChannel socketChannel) {
		ChannelId channelId = new BasicChannelId();
		connectedSocketChanels.put(channelId, socketChannel);
		logger.info(String.format("Assigned channel %s to socket locally bound to %s to remote destination %s.", channelId, getLocalSocketAddress(socketChannel), getRemoteSocketAddress(socketChannel)));
		return channelId;
	}

	public List<SocketChannel> getChannels() {
		return new ArrayList<>(connectedSocketChanels.values());
	}

	public SocketChannel getChannel(ChannelId channelId) {
		SocketChannel socketChannel = connectedSocketChanels.get(channelId);
		if (socketChannel == null) {
			throw new IllegalArgumentException("No SocketChannel can be found for the ChannelId: " + channelId);
		}
		return socketChannel;
	}

	public void closeChannel(ChannelId channelId) {
		SocketChannel socketChannel = connectedSocketChanels.get(channelId);
		if (socketChannel == null) {
			logger.warn("Channel does not exist: " + channelId);
			return;
		}
		logger.info(String.format("Cleaning up socket with a channel id %s.", channelId));
		TcpServerDataLinkServiceConnector.closeChannel(socketChannel);
		connectedSocketChanels.remove(channelId);
	}

	public void closeAll() {
		for (ChannelId channelId : new ArrayList<>(connectedSocketChanels.keySet())) {
			SocketChannel socketChannel = connectedSocketChanels.remove(channelId);
			if (socketChannel != null) {
				TcpServerDataLinkServiceConnector.closeChannel(socketChannel);
			}
		}
	}
}
