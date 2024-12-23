/**
 * Copyright 2016 Graeme Farquharson
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import javax.websocket.DecodeException;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.GenericMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class GenericMessageDecoder implements Decoder.Text<Message> {
	private final Gson gson = new GsonBuilder().setPrettyPrinting().serializeSpecialFloatingPointValues().create();
	private static final Logger LOGGER = LoggerFactory.getLogger(GenericMessageDecoder.class);

	public void init(EndpointConfig config) {
	}
	
	public void destroy() {
	}

	public Message decode(String data) throws DecodeException {
		GenericMessageRegistry registry = GenericMessageRegistryProvider.getRegistry();
		GenericMessage genericMessage = gson.fromJson(data, GenericMessage.class);
		if (registry.isRegistered(genericMessage.getType())) {
			return gson.fromJson(data, registry.get(genericMessage.getType()));
		}
		LOGGER.warn("No decoder registered for " + genericMessage.getType());
		throw new IllegalArgumentException("Failed to decode message: " + data);
	}

	public boolean willDecode(String message) {
		return true;
	}
}
