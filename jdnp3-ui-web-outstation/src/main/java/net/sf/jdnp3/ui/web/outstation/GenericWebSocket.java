package net.sf.jdnp3.ui.web.outstation;

import static java.lang.String.format;

import java.util.List;

import javax.websocket.CloseReason;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.jdnp3.ui.web.outstation.database.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseListener;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value="/secure/ws/general", encoders=MessageEncoder.class, decoders=GenericMessageDecoder.class, configurator=GenericWebSocketConfigurator.class)
public class GenericWebSocket implements DatabaseListener {
	private Logger logger = LoggerFactory.getLogger(GenericWebSocket.class);
	
	private Session session;
	private DatabaseManager databaseManager;
	
	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		if (!session.getRequestParameterMap().containsKey("stationCode") || !session.getRequestParameterMap().containsKey("deviceCode")) {
			String reason = "A stationCode and deviceCode must be defined.";
			tryCloseSession(session, reason);
			logger.warn(reason);
			return;
		}
		
		String stationCode = session.getRequestParameterMap().get("stationCode").get(0);
		String deviceCode = session.getRequestParameterMap().get("deviceCode").get(0);
		try {
			databaseManager = DatabaseManagerProvider.getDatabaseManager(stationCode, deviceCode);
		} catch (Exception e) {
			String reason = format("Cannot from station %s and device %s.", stationCode, deviceCode);
			tryCloseSession(session, reason);
			logger.warn(reason);
			return;
		}
		databaseManager.addDatabaseListener(this);
		
		this.session = session;
    	session.setMaxIdleTimeout(0);
    	
		this.valueChanged(databaseManager.getInternalIndicatorsDataPoint());
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			this.valueChanged(binaryDataPoint);
		}
		List<BinaryOutputDataPoint> binaryOutputDataPoints = databaseManager.getBinaryOutputDataPoints();
		for (BinaryOutputDataPoint binaryDataPoint : binaryOutputDataPoints) {
			this.valueChanged(binaryDataPoint);
		}
		List<AnalogInputDataPoint> analogDataPoints = databaseManager.getAnalogInputDataPoints();
		for (AnalogInputDataPoint analogDataPoint : analogDataPoints) {
			this.valueChanged(analogDataPoint);
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
    	if (databaseManager != null) {
    		databaseManager.removeDatabaseListener(this);
    	}
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    	if (databaseManager != null) {
    		databaseManager.removeDatabaseListener(this);
    	}
    	logger.error("Websocket Error", throwable);
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
				BeanUtils.copyProperties(message, dataPoint);
				session.getAsyncRemote().sendObject(message);
			} catch (Exception e) {
				logger.error("Cannot create message.", e);
			}
		} else {
			logger.warn(format("Data point type %s has not been mapped to a message.", dataPoint.getClass()));
		}
	}
	
	public DatabaseManager getDatabaseManager() {
		if (databaseManager == null) {
			throw new IllegalStateException("No database manager has been subscribed to.");
		}
		return databaseManager;
	}

	private void tryCloseSession(Session session, String reason) {
		try {
			CloseReason closeReason = new CloseReason(CloseReason.CloseCodes.CANNOT_ACCEPT, reason);
			session.close(closeReason);
		} catch (Exception e) {
			logger.error("Failed to close session.", e);
		}
	}
}
