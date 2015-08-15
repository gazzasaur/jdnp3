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

import static java.util.Arrays.asList;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpIpDataSourceWorker implements Runnable {
	private Logger logger = LoggerFactory.getLogger(TcpIpDataSourceWorker.class);
	
	private Selector selector;
	private Object selectionLock = new Object();
	
	public TcpIpDataSourceWorker() {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			throw new RuntimeException("Cannot create selector required for data source.", e);
		}
	}
	
	public void registerSource(SocketChannel socketChannel, ConcurrentLinkedDeque<Byte> dataBuffer) {
		try {
			synchronized (selectionLock) {
				selector.wakeup();
				socketChannel.register(selector, SelectionKey.OP_READ, dataBuffer);
			}
		} catch (ClosedChannelException e) {
			logger.error("Failed to register a socket to the socket channel.");
		}
	}

	@SuppressWarnings("unchecked")
	public void run() {
		while (true) {
			ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
			try {
				synchronized (selectionLock) {
				}
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					ConcurrentLinkedDeque<Byte> dataBuffer = (ConcurrentLinkedDeque<Byte>) selectionKey.attachment();
					int count = socketChannel.read(byteBuffer);
					if (count < 0) {
						selectionKey.cancel();
						continue;
					}
					Byte[] byteData = new Byte[count];
					for (int i = 0; i < count; ++i) {
						byteData[i] = byteBuffer.array()[i];
					}
					dataBuffer.addAll(asList(byteData));
					synchronized (dataBuffer) {
						dataBuffer.notify();
					}
					iterator.remove();
					byteBuffer.clear();
				}
			} catch (Exception e) {
				logger.error("Failed during read loop.", e);
				tryCloseSelector();
				return;
			}
		}
	}

	private void tryCloseSelector() {
		try {
			selector.close();
		} catch (IOException e) {
			logger.error("Failed to close selector.", e);
		}
	}
}
