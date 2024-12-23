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

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.decoder.DataLinkDigester;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpListener;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class SocketChannelDataPumpListener implements DataPumpListener {
	private Logger logger = LoggerFactory.getLogger(SocketChannelDataPumpListener.class);
	
	private ChannelId channelId;
	private ChannelManager channelManager;
	private DataLinkInterceptor dataLinkInterceptor;
	private DataLinkDigester dataLinkDigester = new DataLinkDigester();

	public SocketChannelDataPumpListener(ChannelId channelId, ChannelManager channelManager, DataLinkInterceptor dataLinkInterceptor) {
		this.channelId = channelId;
		this.channelManager = channelManager;
		this.dataLinkInterceptor = dataLinkInterceptor;
		this.dataLinkInterceptor.connected(channelId);
	}
	
	public void connected() {
	}

	public void disconnected() {
		dataLinkInterceptor.disconnected(channelId);
		channelManager.closeChannel(channelId);
	}

	public void dataReceived(List<Byte> data) {
		try {
			logger.debug(format("Data received on channel %s: %s", channelId, DataUtils.toString(data)));
			if (dataLinkDigester.digest(data)) {
				MessageProperties messageProperties = new MessageProperties();
				DataLinkFrame dataLinkFrame = dataLinkDigester.getDataLinkFrame();
				messageProperties.setChannelId(channelId);
				messageProperties.setTimeReceived(new Date().getTime());
				messageProperties.setPrimary(dataLinkFrame.getDataLinkFrameHeader().isPrimary());
				messageProperties.setSourceAddress(dataLinkFrame.getDataLinkFrameHeader().getSource());
				messageProperties.setDestinationAddress(dataLinkFrame.getDataLinkFrameHeader().getDestination());
				messageProperties.setMaster(dataLinkFrame.getDataLinkFrameHeader().getDirection().equals(MASTER_TO_OUTSTATION));
				dataLinkInterceptor.receiveData(messageProperties, dataLinkFrame);
				logger.debug("Frame Received\n" + DataLinkFrameUtils.toString(channelId, dataLinkFrame));
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Failed to process message.", e);
		}
	}
}
