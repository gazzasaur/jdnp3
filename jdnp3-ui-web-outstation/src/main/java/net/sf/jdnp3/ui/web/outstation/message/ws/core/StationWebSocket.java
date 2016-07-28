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

	public void updatedSiteList(SiteListing siteList) {
		SiteListingMessage siteListingMessage = new SiteListingMessage();
		siteListingMessage.setSiteDeviceLists(siteList.getSiteDeviceLists());
		this.sendMessage(siteListingMessage);
	}
}
