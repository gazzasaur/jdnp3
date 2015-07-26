package net.sf.jdnp3.ui.web.outstation;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EndpointConfig;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

import org.apache.commons.beanutils.BeanUtils;
import org.eclipse.jetty.websocket.common.SessionListener;
import org.eclipse.jetty.websocket.common.WebSocketSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ServerEndpoint(value="/secure/ws/general", encoders=MessageEncoder.class, decoders=BinaryInputMessageDecoder.class, configurator=GenericWebSocketConfigurator.class)
public class GenericWebSocket implements SessionListener {
	private Logger logger = LoggerFactory.getLogger(GenericWebSocket.class);
	
	private static List<Session> connectedSessions = new ArrayList<>();
	
	private Session session;
	private HttpSession httpSession;
	
	// FIXME IMPL Do I really need these and to implement SessionListener?
	public void onSessionOpened(WebSocketSession webSocketSession) {
	}
	
	public void onSessionClosed(WebSocketSession arg0) {
	}
	
	@OnOpen
	public void onConnect(Session session, EndpointConfig endpointConfig) {
		System.out.println("Connected");
		this.session = session;
		this.httpSession = (HttpSession) endpointConfig.getUserProperties().get(HttpSession.class.getName());
		connectedSessions.add(session);
	}
	
    @OnMessage
    public void onMessage(Session session, Message msg) {
    	if (msg instanceof BinaryInputMessage) {
    		BinaryDataPoint binaryDataPoint = new BinaryDataPoint();
    		try {
				BeanUtils.copyProperties(binaryDataPoint, msg);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}
    		DatabaseManagerProvider.getDatabaseManager().setBinaryDataPoint(binaryDataPoint);
    		for (Session connectedSession : connectedSessions) {
				connectedSession.getAsyncRemote().sendObject(msg);
			}
    	}
    }
    
    @OnClose
    public void onClose(Session session) {
		connectedSessions.remove(session);
    }
    
    @OnError
    public void onError(Session session, Throwable throwable) {
    	throwable.printStackTrace();
    }
    
	private boolean validate(Session session) {
		if (session.getUserPrincipal() == null) {
			logger.warn("Blocked unauthenticated websocket.");
			tryClose(session, new CloseReason(CloseCodes.VIOLATED_POLICY, "The session is not authenticated."));
			return false;
		}
		return true;
	}
	
	private void tryClose(Session session, CloseReason closeReason) {
		try {
			session.close(closeReason);
		} catch (IOException e) {
			logger.error("Failed to close websocket.", e);
		}
	}
}
