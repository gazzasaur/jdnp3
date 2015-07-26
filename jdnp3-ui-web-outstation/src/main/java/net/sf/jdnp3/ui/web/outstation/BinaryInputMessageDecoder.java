package net.sf.jdnp3.ui.web.outstation;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class BinaryInputMessageDecoder implements Decoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void init(EndpointConfig config) {
	}
	
	public void destroy() {
	}

	public Message decode(String data) throws DecodeException {
		return gson.fromJson(data, BinaryInputMessage.class);
	}

	public boolean willDecode(String message) {
		return message.contains("binaryInput");
	}
}
