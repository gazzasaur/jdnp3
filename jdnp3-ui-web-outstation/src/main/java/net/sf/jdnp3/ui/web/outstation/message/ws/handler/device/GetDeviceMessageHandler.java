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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.device;

import java.util.ArrayList;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceManager;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.GetDeviceMessage;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetDeviceMessageHandler implements MessageHandler {
	private Logger logger = LoggerFactory.getLogger(GetDeviceMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof GetDeviceMessage;
	}

	public void processMessage(DeviceManager webSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		GetDeviceMessage specificMessage = (GetDeviceMessage) message;
		specificMessage.setBinaryInputPoints(new ArrayList<>());

		DatabaseManager databaseManager = webSocket.getDatabaseManager();
		try {
			InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
			specificMessage.setInternalIndicators(internalIndicatorsDataPoint);
			
			for (BinaryInputDataPoint point : databaseManager.getBinaryInputDataPoints()) {
				BinaryInputMessage part = new BinaryInputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getBinaryInputPoints().add(part);
			}
			for (BinaryOutputDataPoint point : databaseManager.getBinaryOutputDataPoints()) {
				BinaryOutputMessage part = new BinaryOutputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getBinaryOutputPoints().add(part);
			}
			for (AnalogInputDataPoint point : databaseManager.getAnalogInputDataPoints()) {
				AnalogInputMessage part = new AnalogInputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getAnalogInputPoints().add(part);
			}
		} catch (Exception e) {
			logger.error("Failed to copy object.", e);
		}
		
		webSocket.sendMessage(specificMessage);
	}
}