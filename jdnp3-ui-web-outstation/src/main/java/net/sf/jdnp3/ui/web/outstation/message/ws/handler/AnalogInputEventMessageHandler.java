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

import net.sf.jdnp3.ui.web.outstation.GenericWebSocket;
import net.sf.jdnp3.ui.web.outstation.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.AnalogInputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

public class AnalogInputEventMessageHandler implements MessageHandler {
	public boolean canHandle(Message message) {
		return message instanceof AnalogInputEventMessage;
	}

	public void processMessage(GenericWebSocket genericWebSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		AnalogInputEventMessage analogInputEventMessage = (AnalogInputEventMessage) message;
		DatabaseManagerProvider.getDatabaseManager().triggerAnalogInputEvent(analogInputEventMessage.getIndex());
	}
}
