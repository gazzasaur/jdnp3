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

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class AnalogOutputMessageHandler implements DeviceMessageHandler {
	private Logger logger = LoggerFactory.getLogger(AnalogOutputMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof AnalogOutputMessage;
	}

	public void processMessage(Messanger messanger, DatabaseManager databaseManager, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		AnalogOutputMessage analogOutputMessage = (AnalogOutputMessage) message;

		AnalogOutputDataPoint analogDataPoint = new AnalogOutputDataPoint();
		try {
			BeanUtils.copyProperties(analogDataPoint, analogOutputMessage);
			databaseManager.setAnalogOutputDataPoint(analogDataPoint);
		} catch (Exception e) {
			logger.error("Failed to copy object.", e);
		}
	}
}
