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

import java.util.Date;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.datalink.decoder.DataLinkDigester;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPumpListener;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SocketChannelDataPumpListener implements DataPumpListener {
	private Logger logger = LoggerFactory.getLogger(SocketChannelDataPumpListener.class);
	
	private ChannelId channelId;
	private ChannelManager channelManager;
	private DataLinkListener dataLinkListener;
	private DataLinkDigester dataLinkDigester = new DataLinkDigester();

	public SocketChannelDataPumpListener(ChannelId channelId, ChannelManager channelManager, DataLinkListener dataLinkListener) {
		this.channelId = channelId;
		this.channelManager = channelManager;
		this.dataLinkListener = dataLinkListener;
	}
	
	public void connected() {
	}

	public void disconnected() {
		channelManager.closeChannel(channelId);
	}

	public void dataReceived(List<Byte> data) {
		try {
			if (dataLinkDigester.digest(data)) {
				logger.debug(String.format("Data received on channel %s: %s", channelId, DataUtils.toString(data)));
				
				MessageProperties messageProperties =new MessageProperties();
				DataLinkFrame dataLinkFrame = dataLinkDigester.getDataLinkFrame();
				messageProperties.setChannelId(channelId);
				messageProperties.setTimeReceived(new Date().getTime());
				messageProperties.setSourceAddress(dataLinkFrame.getDataLinkFrameHeader().getSource());
				messageProperties.setDestinationAddress(dataLinkFrame.getDataLinkFrameHeader().getDestination());
				messageProperties.setMaster(dataLinkFrame.getDataLinkFrameHeader().getDirection().equals(MASTER_TO_OUTSTATION));
				dataLinkListener.receiveData(messageProperties, dataLinkFrame.getData());
			}
		} catch (Exception e) {
			logger.error("Failed to process message.", e);
		}
	}
}
