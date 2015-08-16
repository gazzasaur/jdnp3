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

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.ui.web.outstation.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.database.BinaryDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryInputMessageHandler implements MessageHandler {
	private Logger logger = LoggerFactory.getLogger(BinaryInputMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof BinaryInputMessage;
	}

	public void processMessage(Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		BinaryInputMessage binaryInputMessage = (BinaryInputMessage) message;

		BinaryDataPoint binaryDataPoint = new BinaryDataPoint();
		try {
			BeanUtils.copyProperties(binaryDataPoint, binaryInputMessage);
			binaryDataPoint.setStaticType(new ObjectType(1, binaryInputMessage.getStaticVariation()));
			binaryDataPoint.setEventType(new ObjectType(2, binaryInputMessage.getEventVariation()));
			DatabaseManagerProvider.getDatabaseManager().setBinaryDataPoint(binaryDataPoint);
		} catch (Exception e) {
			logger.error("Failed to copy object.", e);
		}
	}
}
