package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenericMessageDecoder implements Decoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

	public void init(EndpointConfig config) {
	}
	
	public void destroy() {
	}

	public Message decode(String data) throws DecodeException {
		if (data.contains("binaryInputPoint")) {
			return gson.fromJson(data, BinaryInputMessage.class);
		} else if (data.contains("binaryInputEvent")) {
			return gson.fromJson(data, BinaryInputEventMessage.class);
		}
		return null;
	}

	public boolean willDecode(String message) {
		return true;
	}
}
