package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.util.List;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseListener;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.encoder.MessageEncoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.datalink.OutstationBindingsMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.ModelChangedMessage;

@ServerEndpoint(value="/ws/device", encoders=MessageEncoder.class, decoders=GenericMessageDecoder.class, configurator=DeviceWebSocketConfigurator.class)
public class DeviceWebSocket implements Messanger, DatabaseListener {
	private Logger logger = LoggerFactory.getLogger(DeviceWebSocket.class);
	
	private Session session;

	private String device = "";
	private String station = "";
	
	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		this.session = session;
		session.setMaxIdleTimeout(0);
		
		try {
			station = (String) defaultIfNull(session.getRequestParameterMap().get("stationCode").get(0), "");
			device = (String) defaultIfNull(session.getRequestParameterMap().get("deviceCode").get(0), "");
		} catch (Exception e) {
		}
    	
		if (!station.isEmpty() && !device.isEmpty()) {
			DeviceProvider.addDatabaseListener(station, device, this);
		}
	}
	
    @OnMessage
    public void onMessage(Session session, Message message) {
    	MessageHandlerRegistry messageHandlerRegistry = MessageHandlerRegistryProvider.getMessageHandlerRegistry();
    	MessageHandler messageHandler = messageHandlerRegistry.fetchMessageHandler(message);
   		messageHandler.processMessage(this, message);
    }
	
    @OnClose
    public void onClose(Session session) {
    	DeviceProvider.removeDatabaseListener(station, device, this);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    	logger.error("Websocket Error", throwable);
    	DeviceProvider.removeDatabaseListener(station, device, this);
    }
    
    public void sendMessage(Message message) {
    	session.getAsyncRemote().sendObject(message);
    }

	public void modelChanged() {
		session.getAsyncRemote().sendObject(new ModelChangedMessage());
	}

	public void valueChanged(DataPoint dataPoint) {
		GenericMessageRegistry registry = GenericMessageRegistryProvider.getRegistry();
		if (registry.isRegistered(dataPoint.getClass())) {
			try {
				Class<? extends Message> messageClass = registry.get(dataPoint.getClass());
				Message message = messageClass.newInstance();
				if (message instanceof DeviceMessage) {
					DeviceMessage deviceMessage = (DeviceMessage) message;
					deviceMessage.setSite(station);
					deviceMessage.setDevice(device);
				}
				
				BeanUtils.copyProperties(message, dataPoint);
				session.getAsyncRemote().sendObject(message);
			} catch (Exception e) {
				logger.error("Cannot create message.", e);
			}
		} else {
			logger.warn(format("Data point type %s has not been mapped to a message.", dataPoint.getClass()));
		}
	}

	public void bindingsChanged(List<OutstationBinding> outstationBindings) {
		try {
			OutstationBindingsMessage message = new OutstationBindingsMessage();
			message.setSite(station);
			message.setDevice(device);
			message.setOutstationBindings(outstationBindings);
			session.getAsyncRemote().sendObject(message);
		} catch (Exception e) {
			logger.error("Cannot create message.", e);
		}
	}
}
