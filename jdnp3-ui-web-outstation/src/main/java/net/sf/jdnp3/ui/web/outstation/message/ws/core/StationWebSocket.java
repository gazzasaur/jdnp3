package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProviderListener;
import net.sf.jdnp3.ui.web.outstation.main.SiteListing;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.encoder.MessageEncoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SiteListingMessage;

@ServerEndpoint(value="/ws/station", encoders=MessageEncoder.class, decoders=GenericMessageDecoder.class, configurator=StationWebSocketConfigurator.class)
public class StationWebSocket implements Messanger, DeviceProviderListener {
	private Logger logger = LoggerFactory.getLogger(StationWebSocket.class);
	
	private Session session;

	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		this.session = session;
		session.setMaxIdleTimeout(0);
		
		DeviceProvider.addDeviceProviderListener(this);
	}
	
    @OnMessage
    public void onMessage(Session session, Message message) {
    }
	
    @OnClose
    public void onClose(Session session) {
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    	logger.error("Websocket Error", throwable);
    }
    
    public void sendMessage(Message message) {
    	session.getAsyncRemote().sendObject(message);
    }

	public void updatedSteList(SiteListing siteList) {
		SiteListingMessage siteListingMessage = new SiteListingMessage();
		siteListingMessage.setSiteDeviceLists(siteList.getSiteDeviceLists());
		this.sendMessage(siteListingMessage);
	}
}
