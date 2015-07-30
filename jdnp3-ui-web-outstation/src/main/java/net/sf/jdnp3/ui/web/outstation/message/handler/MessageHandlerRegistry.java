/**
 * Copyright 2015 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.message.handler;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.Message;
import net.sf.jdnp3.ui.web.outstation.MessageHandler;

public class MessageHandlerRegistry {
	private List<MessageHandler> messageHandlers = new ArrayList<>();
	
	public void registerHandler(MessageHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}

	public void unregisterHandler(MessageHandler messageHandler) {
		messageHandlers.remove(messageHandler);
	}

	public MessageHandler fetchMessageHandler(Message message) {
		for (MessageHandler messageHandler : messageHandlers) {
			if (messageHandler.canHandle(message)) {
				return messageHandler;
			}
		}
		throw new IllegalStateException("No message handler found.");
	}
}
