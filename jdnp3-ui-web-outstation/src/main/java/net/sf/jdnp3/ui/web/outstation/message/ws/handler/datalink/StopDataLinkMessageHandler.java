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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.datalink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.FailureMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SuccessMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.datalink.StopDataLinkMessage;

public class StopDataLinkMessageHandler implements DeviceMessageHandler, MessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(StopDataLinkMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof StopDataLinkMessage;
	}

	public void processMessage(Messanger messanger, OutstationDevice outstationDevice, Message message) {
		this.processMessage(messanger, message);
	}

	public void processMessage(Messanger webSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		StopDataLinkMessage specificMessage = (StopDataLinkMessage) message;
		Message responseMessage = new SuccessMessage();

		try {
			DeviceProvider.startDataLinkManager(specificMessage.getDataLink());
		} catch (Exception e) {
			FailureMessage failureMessage = new FailureMessage();
			failureMessage.setReason("Cannot stop datalink binding.  Please check log for details.");
			responseMessage = failureMessage;
			LOGGER.error("Cannot stop datalink binding.", e);
		}
		
		webSocket.sendMessage(responseMessage);
	}
}
