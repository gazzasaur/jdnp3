package net.sf.jdnp3.ui.web.outstation.message.ws.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageEncoder implements Encoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create();

	public void init(EndpointConfig config) {
	}

	public void destroy() {
	}
	
	public String encode(Message message) throws EncodeException {
		return gson.toJson(message);
	}

}
