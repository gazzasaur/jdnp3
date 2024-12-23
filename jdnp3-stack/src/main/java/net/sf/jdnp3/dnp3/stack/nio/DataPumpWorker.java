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
package net.sf.jdnp3.dnp3.stack.nio;

import java.io.IOException;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataPumpWorker implements Runnable {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataPumpWorker.class);
	
	private Selector selector;
	private boolean running = false;
	private Object selectionLock = new Object();
	private static AtomicLong registeredSockets = new AtomicLong();
	
	public DataPumpWorker() {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			throw new RuntimeException("Cannot create selector required for data source.", e);
		}
	}
	
	public void registerServerChannel(SelectableChannel selectableChannel, DataPumpListener dataPumpListener) {
		try {
			synchronized (selectionLock) {
				selector.wakeup();
				if (!selectableChannel.isRegistered()) {
					LOGGER.error("Open sockets: {}", registeredSockets.incrementAndGet());
					selectableChannel.register(selector, SelectionKey.OP_ACCEPT, new DataPumpItem(0, new NullDataPumpTransceiver(), dataPumpListener));
				} else {
					LOGGER.warn("Cannot register a socket channel that is already registered.");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void registerAcceptedChannel(SocketChannel socketChannel, DataPumpListener dataListener) {
		try {
			synchronized (selectionLock) {
				selector.wakeup();
				if (!socketChannel.isRegistered()) {
					LOGGER.error("Open sockets: {}", registeredSockets.incrementAndGet());
					socketChannel.register(selector, SelectionKey.OP_READ, new DataPumpItem(65535, new SocketChannelDataPumpTransceiver(), dataListener));
				} else {
					LOGGER.warn("Cannot register a socket channel that is already registered.");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public void registerChannel(SocketChannel socketChannel, DataPumpListener dataListener) {
		try {
			synchronized (selectionLock) {
				selector.wakeup();
				if (!socketChannel.isRegistered()) {
					LOGGER.error("Open sockets: {}", registeredSockets.incrementAndGet());
					socketChannel.register(selector, SelectionKey.OP_CONNECT, new DataPumpItem(65535, new SocketChannelDataPumpTransceiver(), dataListener));
				} else {
					LOGGER.warn("Cannot register a socket channel that is already registered.");
				}
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendData(SocketChannel socketChannel, List<Byte> data) {
		synchronized (selectionLock) {
			selector.wakeup();
			SelectionKey key = socketChannel.keyFor(selector);
			if (key != null) {
				((DataPumpItem) key.attachment()).getDataPumpSendBuffer().addData(data);
				key.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
			} else {
				LOGGER.warn("Cannot send data to a socket channel that is not registered.");
			}
		}
	}

	public void run() {
		running = true;
		while (running) {
			try {
				synchronized (selectionLock) {
					// This ensures the handlers above execute before select can block again.
				}
				selector.select();
				Set<SelectionKey> selectedKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectedKeys.iterator();
				
				while (iterator.hasNext()) {
					SelectionKey selectionKey = iterator.next();
					DataPumpItem dataPumpItem = (DataPumpItem) selectionKey.attachment();
					DataPumpListener dataPumpListener = dataPumpItem.getDataPumpListener();
					DataPumpTransceiver dataPumpTransceiver = dataPumpItem.getDataPumpTransceiver();
					iterator.remove();

					if (!selectionKey.isValid()) {
						try {
							dataPumpListener.disconnected();
						} catch (Exception e) {
							LOGGER.error("Failed to disconnection. Moving on.", e);
						}
						LOGGER.error("Open sockets: {}", registeredSockets.decrementAndGet());
						continue;
					}

					if (selectionKey.isConnectable()) {
						try {
							((SocketChannel) selectionKey.channel()).finishConnect();
							selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							dataPumpListener.connected();
						} catch (Exception e) {
							LOGGER.error("Failed to connect to end point.", e);
							selectionKey.channel().close();
							dataPumpItem.disconnected();
							selectionKey.cancel();
							continue;
						}
					}

					if (selectionKey.isAcceptable()) {
						try {
							dataPumpListener.connected();
						} catch (Exception e) {
							LOGGER.error("Failed to connect to end point.", e);
							selectionKey.channel().close();
							dataPumpItem.disconnected();
							selectionKey.cancel();
							continue;
						}
					}

					if (selectionKey.isReadable()) {
						try {
							dataPumpTransceiver.read(selectionKey.channel(), dataPumpItem);
						} catch (Exception e) {
							LOGGER.error("Failed to read from end point.", e);
							LOGGER.error("Open sockets: {}", registeredSockets.decrementAndGet());
							selectionKey.channel().close();
							dataPumpItem.disconnected();
							selectionKey.cancel();
							continue;
						}
					}

					if (selectionKey.isWritable()) {
						try {
							if (!dataPumpTransceiver.write(selectionKey.channel(), dataPumpItem)) {
								selectionKey.interestOps(SelectionKey.OP_READ);
							} else {
								selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
							}
						} catch (Exception e) {
							LOGGER.error("Failed to write to end point.", e);
							LOGGER.error("Open sockets: {}", registeredSockets.decrementAndGet());
							selectionKey.channel().close();
							dataPumpItem.disconnected();
							selectionKey.cancel();
							continue;
						}
					}
				}
			} catch (Exception e) {
				LOGGER.error("Catastrophic failure.  Failed during read loop.", e);
				running = false;
			}
		}
	}
}
