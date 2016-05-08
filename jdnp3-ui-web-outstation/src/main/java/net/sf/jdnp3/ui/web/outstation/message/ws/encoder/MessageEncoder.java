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
package net.sf.jdnp3.ui.web.outstation.message.ws.encoder;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

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
