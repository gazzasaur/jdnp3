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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.core;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class DeviceMessageHandlerRegistry implements MessageHandler {
	private List<DeviceMessageHandler> messageHandlers = new ArrayList<>();
	
	public void registerHandler(DeviceMessageHandler messageHandler) {
		messageHandlers.add(messageHandler);
	}

	public void unregisterHandler(DeviceMessageHandler messageHandler) {
		messageHandlers.remove(messageHandler);
	}

	public boolean canHandle(Message message) {
		try {
			this.fetchMessageHandler(message);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public void processMessage(Messanger messanger, Message message) {
		DeviceMessage deviceMessage = (DeviceMessage) message;
		DatabaseManager databaseManager;
		
		try {
			databaseManager = DeviceProvider.getDevice(deviceMessage.getSite(), deviceMessage.getDevice()).getDatabaseManager();
		} catch (Exception e) {
			String reason = format("Cannot find station %s and device %s.", deviceMessage.getSite(), deviceMessage.getDevice());
			throw new RuntimeException(reason, e);
		}

		DeviceMessageHandler messageHandler = this.fetchMessageHandler(message);
		messageHandler.processMessage(messanger, databaseManager, message);
	}

	public DeviceMessageHandler fetchMessageHandler(Message message) {
		for (DeviceMessageHandler messageHandler : messageHandlers) {
			if (messageHandler.canHandle(message)) {
				return messageHandler;
			}
		}
		throw new IllegalStateException("No message handler found: " + message);
	}
}
