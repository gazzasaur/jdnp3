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

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.OUTSTATION_TO_MASTER;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils.headerLengthToRawLength;

import java.io.IOException;
import java.net.InetSocketAddress;
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
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.pump.DataPumpListener;
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.pump.DataPumpWorker;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TcpIpServerDataLink implements Runnable, DataLinkLayer {
	private static final int CHARACTER_TIMEOUT = 2000;

	private Logger logger = LoggerFactory.getLogger(TcpIpServerDataLink.class);
	
	DataPumpWorker tcpDataPumpWorker = new DataPumpWorker();
	
	private volatile SocketChannel socketChannel = null;
	private DataLinkFrameEncoder encoder = new DataLinkFrameEncoderImpl();
	private Deque<DataLinkListener> dataLinkListeners = new ConcurrentLinkedDeque<>();
	
	private int mtu = 253;
	
	private long lastDrop;
	private Thread thread = null;
	private int maximumReceiveDataSize = 10000;
	private List<Byte> frameBuffer = new ArrayList<>();
	private DataLinkFrameDecoder decoder = new DataLinkFrameDecoderImpl();
	private ConcurrentLinkedDeque<Byte> inputDataBuffer = new ConcurrentLinkedDeque<>();
	private DataLinkFrameHeaderDetector detector = new DataLinkFrameHeaderDetector();
	
	public void enable() {
		if (thread == null) {
			new Thread(tcpDataPumpWorker).start();

			thread = new Thread(this);
			thread.start();
		}
	}

	public void sendData(int source, int destination, boolean master, List<Byte> data) {
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrame.getDataLinkFrameHeader().setPrimary(true);
		dataLinkFrame.getDataLinkFrameHeader().setSource(source);
		dataLinkFrame.getDataLinkFrameHeader().setDestination(destination);
		dataLinkFrame.getDataLinkFrameHeader().setFunctionCode(FunctionCode.UNCONFIRMED_USER_DATA);
		dataLinkFrame.getDataLinkFrameHeader().setDirection((master) ? MASTER_TO_OUTSTATION : OUTSTATION_TO_MASTER);
		dataLinkFrame.setData(data);
		
		if (socketChannel != null) {
			tcpDataPumpWorker.sendData(socketChannel, encoder.encode(dataLinkFrame));
		}
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
		try {
			ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.bind(new InetSocketAddress(20000));
			serverSocketChannel.accept();
			
			tcpDataPumpWorker.registerServerChannel(serverSocketChannel, new DataPumpListener() {
				public void connected() {
					try {
						SocketChannel acceptedChannel = serverSocketChannel.accept();
						acceptedChannel.configureBlocking(false);
						socketChannel = acceptedChannel;
					} catch (IOException e) {
						logger.error("Failed to set the socket channel to non-blocking.", e);
					}
					tcpDataPumpWorker.registerChannel(socketChannel, new DataPumpListener() {
						public void connected() {
						}
						
						public void disconnected() {
						}
						
						public void dataReceived(List<Byte> data) {
							inputDataBuffer.addAll(data);
							synchronized (inputDataBuffer) {
								inputDataBuffer.notify();
							}
						}
					});
				}

				public void disconnected() {
				}

				public void dataReceived(List<Byte> data) {
				}
			});
			
			DataLinkFrameHeader dataLinkFrameHeader = new DataLinkFrameHeader();
			
			lastDrop = new Date().getTime();
			while (true) {
				synchronized (inputDataBuffer) {
					inputDataBuffer.wait(100);
				}
				while (!inputDataBuffer.isEmpty()) {
					frameBuffer.add(inputDataBuffer.poll());
				}
				try {
					if (detector.detectHeader(dataLinkFrameHeader, frameBuffer) && headerLengthToRawLength(dataLinkFrameHeader.getLength()) <= frameBuffer.size()) {
						frameReceived(frameBuffer);
						frameBuffer = new ArrayList<>(frameBuffer.subList(headerLengthToRawLength(dataLinkFrameHeader.getLength()), frameBuffer.size()));
						lastDrop = new Date().getTime();
					}
				} catch (Exception e) {
					logger.warn("Error found on stream.", e);
					performRapidDrop(frameBuffer);
				}
				if (frameBuffer.size() == 0) {
					lastDrop = new Date().getTime();
				}
				if (new Date().getTime() - lastDrop > CHARACTER_TIMEOUT) {
					logger.warn("Timeout on stream.");
					performRapidDrop(frameBuffer);
				}
				if (frameBuffer.size() > maximumReceiveDataSize) {
					logger.warn("Receive buffer has exceeded max size.");
					performRapidDrop(frameBuffer);
				}
			}
		} catch (IOException | InterruptedException e) {
			logger.error("Stream failure.", e);
			if (socketChannel != null) {
				try {
					socketChannel.close();
				} catch (IOException ioe) {
					logger.warn("Cannot close socket.", ioe);
				}
			}
		}
	}

	public int getMtu() {
		return mtu;
	}

	public void addDataLinkLayerListener(DataLinkListener listener) {
		dataLinkListeners.add(listener);
	}

	public void removeDataLinkLayerListener(DataLinkListener listener) {
		dataLinkListeners.remove(listener);
	}
	
	private void frameReceived(List<Byte> buffer) {
		if (dataLinkListeners.size() == 0) {
			throw new IllegalStateException("No transport layer has been specified.");
		}
		DataLinkFrame dataLinkFrame = decoder.decode(buffer);
		if (dataLinkFrame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.UNCONFIRMED_USER_DATA && dataLinkFrame.getDataLinkFrameHeader().isPrimary()) {
			for (DataLinkListener dataLinkListener : dataLinkListeners) {
				dataLinkListener.receiveData(dataLinkFrame.getData());
			}
		}
	}
	
	private void performRapidDrop(List<Byte> data) {
		if (data.size() < 1) {
			return;
		}
		List<Byte> droppedData = new ArrayList<>();
		droppedData.add(data.remove(0));
		for (int i = 0; i < mtu; ++i) {
			try {
				DataLinkFrameHeader dataLinkFrameHeader = new DataLinkFrameHeader();
				detector.detectHeader(dataLinkFrameHeader, data);
				break;
			} catch (Exception e) {
				droppedData.add(data.remove(0));
			}
		}
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Dropping: ");
		for (Byte droppedByte : droppedData) {
			stringBuilder.append(String.format("%02X", droppedByte));
		}
		logger.warn(stringBuilder.toString());
		lastDrop = new Date().getTime();
	}
}
