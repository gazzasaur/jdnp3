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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoder;
import net.sf.jdnp3.dnp3.stack.layer.datalink.encoder.DataLinkFrameEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkConsumer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkService;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkServiceBinding;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpWorker;

public class TcpServerDataLinkService implements DataLinkService, DataLinkLayer {
	private static final int MTU = 249;
	
	private int mtu = MTU;
	private List<DataLinkListener> dataLinkListeners = new ArrayList<>();
	private DataLinkFrameEncoder dataLinkFrameEncoder = new DataLinkFrameEncoderImpl();
	
	private ExecutorService executorService = null;
	private DataPumpWorker dataPump = null;
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
		ServerSocketChannel serverSocketChannel = TcpServerDataLinkServiceConnector.create(host, port);
		dataPump.registerServerChannel(serverSocketChannel, new ServerSocketChannelDataPumpListener(dataPump, executorService, channelManager, serverSocketChannel, dataLinkListeners.get(0)));
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
		dataLinkListeners.add(dataLinkListener);
	}

	public void removeDataLinkLayerListener(DataLinkListener listener) {
		dataLinkListeners.remove(listener);
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

		SocketChannel socketChannel = channelManager.getChannel(messageProperties.getChannelId());
		dataPump.sendData(socketChannel, frameData);
	}
	
	public void setDataPump(DataPumpWorker dataPump) {
		this.dataPump = dataPump;
	}

	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}

}
