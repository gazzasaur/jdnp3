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
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkConsumer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkServiceBinding;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

// FIXME IMPL Implement start/stop/running.
public class TcpServerDataLinkService implements DataLinkLayer {
	private Logger logger = LoggerFactory.getLogger(TcpServerDataLinkService.class);
	
	private static final int MTU = 249;
	
	private int mtu = MTU;
	private MultiDataLinkListener multiDataLinkListener = new MultiDataLinkListener();
	private DataLinkFrameEncoder dataLinkFrameEncoder = new DataLinkFrameEncoderImpl();
	
	private ExecutorService executorService = null;
	private DataPump dataPump = null;
	private String host = "0.0.0.0";
	private int port = 20000;
	
	private ChannelManager channelManager = new ChannelManager();
	
	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

	public void start() {
		if (dataPump == null) {
			throw new IllegalStateException("No data pump has been specified.");
		}
		if (executorService == null) {
			throw new IllegalStateException("No executor service has been defined.");
		}
		ServerSocketChannel serverSocketChannel = TcpServerDataLinkServiceConnector.create(getHost(), getPort());
		dataPump.registerServerChannel(serverSocketChannel, new ServerSocketChannelDataPumpListener(dataPump, channelManager, serverSocketChannel, multiDataLinkListener));
	}

	public void stop() {
	}

	public boolean isRunning() {
		return false;
	}

	public DataLinkServiceBinding bind(DataLinkConsumer dataLinkConsumer) {
		throw new UnsupportedOperationException();
	}	

	public void addDataLinkLayerListener(DataLinkListener dataLinkListener) {
		multiDataLinkListener.addDataLinkListener(dataLinkListener);
	}

	public void removeDataLinkLayerListener(DataLinkListener dataLinkListener) {
		multiDataLinkListener.removeDataLinkListener(dataLinkListener);
	}

	public void sendData(MessageProperties messageProperties, List<Byte> data) {
		DataLinkFrame dataLinkFrame = new DataLinkFrame();
		dataLinkFrame.getDataLinkFrameHeader().setPrimary(true);
		dataLinkFrame.getDataLinkFrameHeader().setSource(messageProperties.getSourceAddress());
		dataLinkFrame.getDataLinkFrameHeader().setDestination(messageProperties.getDestinationAddress());
		dataLinkFrame.getDataLinkFrameHeader().setFunctionCode(FunctionCode.UNCONFIRMED_USER_DATA);
		dataLinkFrame.getDataLinkFrameHeader().setDirection((messageProperties.isMaster()) ? MASTER_TO_OUTSTATION : OUTSTATION_TO_MASTER);
		dataLinkFrame.setData(data);
		List<Byte> frameData = dataLinkFrameEncoder.encode(dataLinkFrame);
		
		logger.debug(String.format("Send data to %s from %s using channel %s: %s", messageProperties.getDestinationAddress(), messageProperties.getSourceAddress(), messageProperties.getChannelId(), DataUtils.toString(frameData)));

		SocketChannel socketChannel = channelManager.getChannel(messageProperties.getChannelId());
		dataPump.sendData(socketChannel, frameData);
	}
	
	public void setDataPump(DataPump dataPump) {
		this.dataPump = dataPump;
	}

	public void setExecutorService(ExecutorService executorService) {
		multiDataLinkListener.setExecutorService(executorService);
		this.executorService = executorService;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getConnectionCount() {
		return channelManager.getChannels().size();
	}
}
