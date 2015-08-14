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

import static java.nio.ByteBuffer.wrap;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpIpDataSinkWorker implements Runnable {
	private Logger logger = LoggerFactory.getLogger(TcpIpDataSinkWorker.class);
	
	private Selector selector;
	private ConcurrentLinkedDeque<Pair<SocketChannel, ByteBuffer>> socketDataBuffer = new ConcurrentLinkedDeque<>();
	
	public TcpIpDataSinkWorker() {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			throw new RuntimeException("Cannot create selector required for data sink.", e);
		}
	}
	
	public void sendData(SocketChannel socketChannel, List<Byte> data) {
		ByteBuffer byteBuffer = wrap(toPrimitive(data.toArray(new Byte[0])));
		socketDataBuffer.add(new ImmutablePair<SocketChannel, ByteBuffer>(socketChannel, byteBuffer));
		byteBuffer.flip();
		selector.wakeup();
	}

	public void run() {
		while (true) {
			try {
				Iterator<Pair<SocketChannel, ByteBuffer>> iterator = socketDataBuffer.iterator();
				while (iterator.hasNext()) {
					Pair<SocketChannel, ByteBuffer> dataPair = iterator.next();
					if (dataPair.getLeft().keyFor(selector) == null) {
						iterator.remove();
						dataPair.getLeft().register(selector, SelectionKey.OP_WRITE, dataPair.getRight());
					}
				}
				
				selector.select();
				Iterator<SelectionKey> keys = selector.selectedKeys().iterator();
				while (keys.hasNext()) {
					SelectionKey selectionKey = keys.next();
					SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
					ByteBuffer dataBuffer = (ByteBuffer) selectionKey.attachment();
					socketChannel.write(dataBuffer);
					keys.remove();

					if (!dataBuffer.hasRemaining()) {
						selectionKey.cancel();
					}
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
