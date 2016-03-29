package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

public class StationWebSocketConfigurator extends ServerEndpointConfig.Configurator {
	@Override
	public void modifyHandshake(ServerEndpointConfig config, HandshakeRequest request, HandshakeResponse response) {
		HttpSession httpSession = (HttpSession) request.getHttpSession();
		config.getUserProperties().put(HttpSession.class.getName(), httpSession);
	}
}
