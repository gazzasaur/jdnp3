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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.datalink;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.FailureMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SuccessMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.datalink.StartDataLinkMessage;

public class StartDataLinkMessageHandler implements MessageHandler {
	private Logger logger = LoggerFactory.getLogger(StartDataLinkMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof StartDataLinkMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		StartDataLinkMessage specificMessage = (StartDataLinkMessage) message;
		Message responseMessage = new SuccessMessage();

		try {
			DataLinkManager dataLinkManager = DataLinkManagerProvider.getDataLinkManager(specificMessage.getDataLink());
			dataLinkManager.start();
		} catch (Exception e) {
			FailureMessage failureMessage = new FailureMessage();
			failureMessage.setReason("Cannot start datalink binding.  Please check log for details.");
			responseMessage = failureMessage;
			logger.error("Cannot start datalink binding.", e);
		}
		
		messanger.sendMessage(responseMessage);
	}
}
