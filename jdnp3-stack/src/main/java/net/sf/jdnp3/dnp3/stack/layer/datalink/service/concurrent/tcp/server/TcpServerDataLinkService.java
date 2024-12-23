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

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.OUTSTATION_TO_MASTER;

import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.List;
import java.util.concurrent.ExecutorService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoder;
import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.MultiDataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class TcpServerDataLinkService implements DataLinkLayer {
	private static final Logger LOGGER = LoggerFactory.getLogger(TcpServerDataLinkService.class);
	
	private static final int MTU = 249;
	
	private int mtu = MTU;
	private boolean closed = false;
	private MultiDataLinkInterceptor multiDataLinkInterceptor = new MultiDataLinkInterceptor();
	private DataLinkFrameEncoder dataLinkFrameEncoder = new DataLinkFrameEncoderImpl();

	private ExecutorService executorService = null;
	private DataPump dataPump = null;
	private String host = "0.0.0.0";
	private int port = 20000;
	
	private ChannelManager channelManager = new ChannelManager();
	private ServerSocketChannel serverSocketChannel = null;
	
	public synchronized int getMtu() {
		return mtu;
	}

	public synchronized void setMtu(int mtu) {
		this.mtu = mtu;
	}

	public synchronized void start() {
		if (closed) {
			throw new IllegalStateException("DataLink has been closed and may not be restarted.");
		}
		if (dataPump == null) {
			throw new IllegalStateException("No data pump has been specified.");
		}
		if (executorService == null) {
			throw new IllegalStateException("No executor service has been defined.");
		}
		if (serverSocketChannel != null) {
			throw new IllegalStateException("Service already started.");
		}
		serverSocketChannel = TcpServerDataLinkServiceConnector.create(getHost(), getPort());
		dataPump.registerServerChannel(serverSocketChannel, new ServerSocketChannelDataPumpListener(dataPump, channelManager, serverSocketChannel, multiDataLinkInterceptor));
	}

	public synchronized void stop() {
		if (serverSocketChannel == null) {
			return;
		}
		TcpServerDataLinkServiceConnector.closeChannel(serverSocketChannel);
		channelManager.closeAll();
		serverSocketChannel = null;
	}

	public synchronized boolean isRunning() {
		return serverSocketChannel != null;
	}
	
	public synchronized void close() {
		closed = true;
		this.stop();
	}

	public synchronized void addDataLinkLayerListener(DataLinkInterceptor dataLinkInterceptor) {
		if (closed) {
			throw new IllegalStateException("DataLink has been closed and may not be restarted.");
		}
		multiDataLinkInterceptor.addDataLinkListener(dataLinkInterceptor);
	}

	public synchronized void removeDataLinkLayerListener(DataLinkInterceptor dataLinkInterceptor) {
		multiDataLinkInterceptor.removeDataLinkListener(dataLinkInterceptor);
	}

	public synchronized void sendData(MessageProperties messageProperties, List<Byte> data) {
		if (closed) {
			throw new IllegalStateException("DataLink has been closed and may not be restarted.");
		}
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrame.getDataLinkFrameHeader().setPrimary(messageProperties.isPrimary());
		dataLinkFrame.getDataLinkFrameHeader().setSource(messageProperties.getSourceAddress());
		dataLinkFrame.getDataLinkFrameHeader().setDestination(messageProperties.getDestinationAddress());
		dataLinkFrame.getDataLinkFrameHeader().setFunctionCode(FunctionCode.UNCONFIRMED_USER_DATA);
		dataLinkFrame.getDataLinkFrameHeader().setDirection((messageProperties.isMaster()) ? MASTER_TO_OUTSTATION : OUTSTATION_TO_MASTER);
		dataLinkFrame.setData(data);
		sendData(messageProperties, dataLinkFrame);
	}

	public synchronized void sendData(MessageProperties messageProperties, DataLinkFrame frame) {
		List<Byte> frameData = dataLinkFrameEncoder.encode(frame);
		LOGGER.debug("Send data to {} from {} using channel {}: {}", messageProperties.getDestinationAddress(), messageProperties.getSourceAddress(), messageProperties.getChannelId(), DataUtils.toString(frameData));
		SocketChannel socketChannel = channelManager.getChannel(messageProperties.getChannelId());
		dataPump.sendData(socketChannel, frameData);
	}

	public synchronized void setDataPump(DataPump dataPump) {
		this.dataPump = dataPump;
	}

	public synchronized void setExecutorService(ExecutorService executorService) {
		multiDataLinkInterceptor.setExecutorService(executorService);
		this.executorService = executorService;
	}

	public synchronized String getHost() {
		return host;
	}

	public synchronized void setHost(String host) {
		this.host = host;
	}

	public synchronized int getPort() {
		return port;
	}

	public synchronized void setPort(int port) {
		this.port = port;
	}
	
	public synchronized int getConnectionCount() {
		return channelManager.getChannels().size();
	}
}
