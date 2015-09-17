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

import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;

import net.sf.jdnp3.dnp3.stack.layer.datalink.io.DataLinkDigester;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.ChannelId;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.MessageProperties;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketChannelDataPumpListener implements DataPumpListener {
	private Logger logger = LoggerFactory.getLogger(SocketChannelDataPumpListener.class);
	
	private ChannelId channelId;
	private ChannelManager channelManager;
	private ExecutorService executorService;
	private DataLinkListener dataLinkListener;
	private DataLinkDigester dataLinkDigester = new DataLinkDigester();

	public SocketChannelDataPumpListener(ChannelId channelId, ExecutorService executorService, ChannelManager channelManager, DataLinkListener dataLinkListener) {
		this.channelId = channelId;
		this.channelManager = channelManager;
		this.executorService = executorService;
		this.dataLinkListener = dataLinkListener;
	}
	
	public void connected() {
	}

	public void disconnected() {
		channelManager.closeChannel(channelId);
	}

	public void dataReceived(List<Byte> data) {
		executorService.execute(new Runnable() {
			public void run() {
				try {
					synchronized (dataLinkListener) {
						if (dataLinkDigester.digest(data)) {
							MessageProperties messageProperties =new MessageProperties();
							DataLinkFrame dataLinkFrame = dataLinkDigester.getDataLinkFrame();
							messageProperties.setChannelId(channelId);
							messageProperties.setTimeReceived(new Date().getTime());
							messageProperties.setSourceAddress(dataLinkFrame.getDataLinkFrameHeader().getSource());
							messageProperties.setDestinationAddress(dataLinkFrame.getDataLinkFrameHeader().getDestination());
							dataLinkListener.receiveData(messageProperties, dataLinkFrame.getData());
						}
					}
				} catch (Exception e) {
					logger.error("Failed to process message.", e);
				}
			}
		});
	}
}
