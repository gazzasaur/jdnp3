package net.sf.jdnp3.ui.web.outstation;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessageEncoder implements Encoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void init(EndpointConfig config) {
	}

	public void destroy() {
	}
	
	public String encode(Message message) throws EncodeException {
		return gson.toJson(message);
	}

}
