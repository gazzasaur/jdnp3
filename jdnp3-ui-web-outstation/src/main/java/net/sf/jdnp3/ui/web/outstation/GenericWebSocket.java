package net.sf.jdnp3.ui.web.outstation;

import java.util.List;

import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseListener;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value="/secure/ws/general", encoders=MessageEncoder.class, decoders=GenericMessageDecoder.class, configurator=GenericWebSocketConfigurator.class)
public class GenericWebSocket implements DatabaseListener {
	private Logger logger = LoggerFactory.getLogger(GenericWebSocket.class);
	
	private Session session;
	
	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		this.session = session;
		DatabaseManagerProvider.getDatabaseManager().addDatabaseListener(this);
		
		List<BinaryDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
		for (BinaryDataPoint binaryDataPoint : binaryDataPoints) {
			this.valueChanged(binaryDataPoint);
		}
	}
	
    @OnMessage
    public void onMessage(Session session, Message message) {
    	MessageHandlerRegistry messageHandlerRegistry = MessageHandlerRegistryProvider.getMessageHandlerRegistry();
    	MessageHandler messageHandler = messageHandlerRegistry.fetchMessageHandler(message);
   		messageHandler.processMessage(message);
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

	public void modelChanged() {
		session.getAsyncRemote().sendObject(new ModelChangedMessage());
	}

	public void valueChanged(BinaryDataPoint binaryDataPoint) {
		BinaryInputMessage binaryInputMessage = new BinaryInputMessage();
		try {
			BeanUtils.copyProperties(binaryInputMessage, binaryDataPoint);
			binaryInputMessage.setStaticVariation(binaryDataPoint.getStaticType().getVariation());
			binaryInputMessage.setEventVariation(binaryDataPoint.getEventType().getVariation());
			session.getAsyncRemote().sendObject(binaryInputMessage);
		} catch (Exception e) {
			logger.error("Cannot create message.", e);
		}
	}
}
