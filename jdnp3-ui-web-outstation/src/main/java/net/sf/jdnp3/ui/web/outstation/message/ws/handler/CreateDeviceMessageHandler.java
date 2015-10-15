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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler;

import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceManager;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.CreateDeviceMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.lang3.ObjectUtils;

public class CreateDeviceMessageHandler implements MessageHandler {
	public boolean canHandle(Message message) {
		return message instanceof CreateDeviceMessage;
	}

	public void processMessage(DeviceManager webSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		CreateDeviceMessage specificMessage = (CreateDeviceMessage) message;
		
		if (ObjectUtils.defaultIfNull(specificMessage.getSiteCode(), "").isEmpty() || ObjectUtils.defaultIfNull(specificMessage.getSiteCode(), "").isEmpty()) {
			throw new IllegalArgumentException("A siteCode and deviceCode must be specified.");
		}

		DatabaseManager databaseManager = DatabaseManagerProvider.registerDevice(specificMessage.getSiteCode(), specificMessage.getDeviceCode());
		databaseManager.clear();
		databaseManager.addBinaryInputDataPoints(specificMessage.getBinaryInputPoints().toArray(new String[0]));
		databaseManager.addBinaryOutputDataPoints(specificMessage.getBinaryOutputPoints().toArray(new String[0]));
		databaseManager.addAnalogInputDataPoints(specificMessage.getAnalogInputPoints().toArray(new String[0]));
	}
}
