package net.sf.jdnp3.dnp3.stack.layer.datalink.service.core;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.message.ChannelId;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public interface DataLinkInterceptor {
	public void connected(ChannelId channelId);
	public void disconnected(ChannelId channelId);
	public void receiveData(MessageProperties messageProperties, DataLinkFrame frame);
}
