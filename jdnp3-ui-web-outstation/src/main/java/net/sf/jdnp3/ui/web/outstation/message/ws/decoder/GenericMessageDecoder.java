package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.GenericMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.HeartbeatMessage;
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
		GenericMessage genericMessage = gson.fromJson(data, GenericMessage.class);
		if (genericMessage.getType().equals("binaryInputPoint")) {
			return gson.fromJson(data, BinaryInputMessage.class);
		} else if (genericMessage.getType().equals("binaryInputEvent")) {
			return gson.fromJson(data, BinaryInputEventMessage.class);
		} else if (genericMessage.getType().equals("heartbeat")) {
			return gson.fromJson(data, HeartbeatMessage.class);
		}
		return null;
	}

	public boolean willDecode(String message) {
		return true;
	}
}
