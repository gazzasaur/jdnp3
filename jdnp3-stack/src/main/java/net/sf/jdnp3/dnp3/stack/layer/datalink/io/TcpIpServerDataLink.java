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
import static org.apache.commons.lang3.ArrayUtils.subarray;
import static org.apache.commons.lang3.ArrayUtils.toObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Date;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

import net.sf.jdnp3.dnp3.stack.layer.datalink.decoder.DataLinkFrameDecoder;
import net.sf.jdnp3.dnp3.stack.layer.datalink.decoder.DataLinkFrameDecoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoder;
import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpIpServerDataLink implements Runnable, DataLinkLayer {
	private static final int CHARACTER_TIMEOUT = 2000;

	private Logger logger = LoggerFactory.getLogger(TcpIpServerDataLink.class);
	
	private int source = 1;
	private int destination = 2;
	private Direction direction = Direction.MASTER_TO_OUTSTATION;
	
	private DataLinkFrameEncoder encoder = new DataLinkFrameEncoderImpl();
	
	private long lastDrop;
	private Thread thread = null;
	private int maximumReceiveDataSize = 10000;
	private TransportLayer transportLayer = null;
	private List<Byte> frameBuffer = new ArrayList<>();
	private Deque<Byte> sendDeque = new ConcurrentLinkedDeque<>();
	private DataLinkFrameDecoder decoder = new DataLinkFrameDecoderImpl();
	private DataLinkFrameHeaderDetector detector = new DataLinkFrameHeaderDetector();
	
	public void enable() {
		if (thread == null) {
			thread = new Thread(this);
			thread.start();
		}
	}

	public void sendData(List<Byte> data) {
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrame.getDataLinkFrameHeader().setPrimary(true);
		dataLinkFrame.getDataLinkFrameHeader().setSource(source);
		dataLinkFrame.getDataLinkFrameHeader().setDirection(direction);
		dataLinkFrame.getDataLinkFrameHeader().setDestination(destination);
		dataLinkFrame.getDataLinkFrameHeader().setFunctionCode(FunctionCode.UNCONFIRMED_USER_DATA);
		dataLinkFrame.setData(data);
		
		sendDeque.addAll(encoder.encode(dataLinkFrame));
	}
	
	public void run() {
		while (true) {
			try {
				tryRun();
			} catch (Exception e) {
				logger.error("Failed during read loop.", e);
			}
			try {
				Thread.sleep(10000);
			} catch (InterruptedException ei) {
				logger.error("Failed during read sleep loop.", ei);
			}
		}
	}
	
	private void tryRun() {
		ServerSocketChannel serverSocketChannel = null;
		SocketChannel socketChannel = null;
		
		try {
			DataLinkFrameHeader dataLinkFrameHeader = new DataLinkFrameHeader();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.bind(new InetSocketAddress(20000));
			socketChannel = serverSocketChannel.accept();
			socketChannel.configureBlocking(false);
			serverSocketChannel.close();
			serverSocketChannel = null;
			
			lastDrop = new Date().getTime();
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			ByteBuffer sendBuffer = ByteBuffer.allocate(1024);
			sendBuffer.flip();
			while (true) {
				if (!sendBuffer.hasRemaining() && sendDeque.size() > 0) {
					sendBuffer.clear();
					while (sendBuffer.hasRemaining() && sendDeque.size() > 0) {
						sendBuffer.put(sendDeque.removeFirst());
					}
					sendBuffer.flip();
				}
				socketChannel.write(sendBuffer);
				
				int result = socketChannel.read(buffer);
				if (result < 0) {
					return;
				} else if (result >= 0) {  
					frameBuffer.addAll(asList(toObject(subarray(buffer.array(), 0, result))));
					buffer.clear();
					try {
						if (detector.detectHeader(dataLinkFrameHeader, frameBuffer) && dataLinkFrameHeader.getLength() + 5 <= frameBuffer.size()) {
							frameReceived(frameBuffer);
							frameBuffer = new ArrayList<>(frameBuffer.subList(dataLinkFrameHeader.getLength() + 7, frameBuffer.size()));
							lastDrop = new Date().getTime();
						}
					} catch (Exception e) {
						logger.warn(String.format("Error found on stream.  Dropping: %02X", (byte) frameBuffer.remove(0)), e);
						lastDrop = new Date().getTime();
					}
					if (result == 0) {
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							logger.warn("Failed to sleep.", e);
						}
					}
				}
				if (frameBuffer.size() == 0) {
					lastDrop = new Date().getTime();
				}
				if (new Date().getTime() - lastDrop > CHARACTER_TIMEOUT) {
					logger.warn(String.format("Timeout on stream.  Dropping: %02X", (byte) frameBuffer.remove(0)));
					lastDrop = new Date().getTime();
				}
				if (frameBuffer.size() > maximumReceiveDataSize) {
					frameBuffer.remove(0);
				}
			}
		} catch (IOException e) {
			logger.error("Stream failure.", e);
			if (serverSocketChannel != null) {
				try {
					serverSocketChannel.close();
				} catch (IOException ioe) {
					logger.warn("Cannot close server socket.", ioe);
				}
			}
			if (socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException ioe) {
					logger.warn("Cannot close socket.", ioe);
				}
			}
		}
	}

	private void frameReceived(List<Byte> buffer) {
		if (transportLayer == null) {
			throw new IllegalStateException("No transport layer has been specified.");
		}
		DataLinkFrame dataLinkFrame = decoder.decode(buffer);
		if (dataLinkFrame.getDataLinkFrameHeader().getDestination() == source &&
				dataLinkFrame.getDataLinkFrameHeader().getSource() == destination &&
				dataLinkFrame.getDataLinkFrameHeader().getDirection() != direction &&
				dataLinkFrame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.UNCONFIRMED_USER_DATA &&
				dataLinkFrame.getDataLinkFrameHeader().isPrimary()) {
			transportLayer.receiveData(dataLinkFrame.getData());
		}
	}

	public int getMaximumReceiveDataSize() {
		return maximumReceiveDataSize;
	}

	public void setMaximumReceiveDataSize(int maximumReceiveDataSize) {
		this.maximumReceiveDataSize = maximumReceiveDataSize;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public void setTransportLayer(TransportLayer transportLayer) {
		this.transportLayer = transportLayer;
	}
}
