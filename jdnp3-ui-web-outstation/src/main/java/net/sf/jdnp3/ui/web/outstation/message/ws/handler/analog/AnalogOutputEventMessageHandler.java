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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.analog;

import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogOutputEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class AnalogOutputEventMessageHandler implements DeviceMessageHandler {
	public boolean canHandle(Message message) {
		return message instanceof AnalogOutputEventMessage;
	}

	public void processMessage(Messanger messanger, OutstationDevice outstationDevice, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		AnalogOutputEventMessage analogOutputEventMessage = (AnalogOutputEventMessage) message;
		System.out.println("Trigger");
		outstationDevice.getDatabaseManager().triggerAnalogOutputEvent(analogOutputEventMessage.getIndex(), analogOutputEventMessage.getTimestamp());
	}
}
