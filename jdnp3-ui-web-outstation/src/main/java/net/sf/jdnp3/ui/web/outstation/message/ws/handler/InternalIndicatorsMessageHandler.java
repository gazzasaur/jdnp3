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
import net.sf.jdnp3.ui.web.outstation.database.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.InternalIndicatorsMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalIndicatorsMessageHandler implements MessageHandler {
	private Logger logger = LoggerFactory.getLogger(InternalIndicatorsMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof InternalIndicatorsMessage;
	}

	public void processMessage(GenericWebSocket genericWebSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		InternalIndicatorsMessage specificMessage = (InternalIndicatorsMessage) message;
		InternalIndicatorsDataPoint dataPoint = new InternalIndicatorsDataPoint();
		try {
			BeanUtils.copyProperties(dataPoint, specificMessage);
			DatabaseManagerProvider.getDatabaseManager().setInternalIndicatorDataPoint(dataPoint);
		} catch (Exception e) {
			logger.error("Failed to copy object.", e);
		}
	}
}
