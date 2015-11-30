package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.GenericMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GenericMessageDecoder implements Decoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create();
	private Logger logger = LoggerFactory.getLogger(GenericMessageDecoder.class);

	public void init(EndpointConfig config) {
	}
	
	public void destroy() {
	}

	public Message decode(String data) throws DecodeException {
		System.out.println(data);
		GenericMessageRegistry registry = GenericMessageRegistryProvider.getRegistry();
		GenericMessage genericMessage = gson.fromJson(data, GenericMessage.class);
		if (registry.isRegistered(genericMessage.getType())) {
			return gson.fromJson(data, registry.get(genericMessage.getType()));
		}
		logger.warn("No decoder registered for " + genericMessage.getType());
		throw new IllegalArgumentException("Failed to decode message: " + data);
	}

	public boolean willDecode(String message) {
		return true;
	}
}
