/**
 * Copyright 2024 Graeme Farquharson
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

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.GlobalDatabaseListener;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.encoder.MessageEncoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

@ServerEndpoint(value="/ws/globalDevice", encoders=MessageEncoder.class, decoders=GenericMessageDecoder.class, configurator=GlobalDeviceWebSocketConfigurator.class)
public class GlobalDeviceWebSocket implements Messanger, GlobalDatabaseListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalDeviceWebSocket.class);
	private static final Executor EXECUTOR = Executors.newSingleThreadExecutor();

	private Session session;

	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		this.session = session;
		session.setMaxIdleTimeout(0);
		DeviceProvider.addGlobalDatabaseListener(this);
	}
	
    @OnMessage
    public void onMessage(Session session, Message message) {
    	MessageHandlerRegistry messageHandlerRegistry = MessageHandlerRegistryProvider.getMessageHandlerRegistry();
    	MessageHandler messageHandler = messageHandlerRegistry.fetchMessageHandler(message);
   		messageHandler.processMessage(this, message);
    }
	
    @OnClose
    public void onClose(Session session) {
    	DeviceProvider.removeGlobalDatabaseListener(this);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    	LOGGER.error("Websocket Error", throwable);
    	DeviceProvider.removeGlobalDatabaseListener(this);
    }
    
    public void sendMessage(Message message) {
		if (session.isOpen()) {
			try {
				session.getAsyncRemote().sendObject(message);
			} catch(Exception e) {
				try {
					session.close();
				} catch (Exception e1) {
				}
				this.onClose(session);
			}
		}
    }

	public void valueChanged(String site, String device, DataPoint dataPoint) {
		GenericMessageRegistry registry = GenericMessageRegistryProvider.getRegistry();
		if (registry.isRegistered(dataPoint.getClass())) {
			try {
				Class<? extends Message> messageClass = registry.get(dataPoint.getClass());
				Message message = messageClass.getDeclaredConstructor().newInstance();
				if (message instanceof DeviceMessage) {
					DeviceMessage deviceMessage = (DeviceMessage) message;
					deviceMessage.setSite(site);
					deviceMessage.setDevice(device);
				}
				
				PropertyUtils.copyProperties(message, dataPoint);
				EXECUTOR.execute(new Runnable() {
					public void run() {
						if (session.isOpen()) {
							try {
								session.getAsyncRemote().sendObject(message);
							} catch (Exception e) {
								LOGGER.error("Failed to send message. Closing Web Socket " + session, e);
								try {
									session.close();
								} catch (Exception wce) {
									LOGGER.warn("Failed to close web socket.", e);
								}
								GlobalDeviceWebSocket.this.onClose(session);
							}
						}
					}
				});
			} catch (Exception e) {
				LOGGER.error("Cannot create message.", e);
			}
		} else {
			LOGGER.warn("Data point type {} has not been mapped to a message.", dataPoint.getClass());
		}
	}
}
