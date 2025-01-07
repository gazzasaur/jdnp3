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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.device;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.InternalIndicatorsMessage;

public class InternalIndicatorsMessageHandler implements DeviceMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(InternalIndicatorsMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof InternalIndicatorsMessage;
	}

	public void processMessage(Messanger messanger, OutstationDevice outstationDevice, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		InternalIndicatorsMessage specificMessage = (InternalIndicatorsMessage) message;
		InternalIndicatorsDataPoint dataPoint = new InternalIndicatorsDataPoint();
		try {
			PropertyUtils.copyProperties(dataPoint, specificMessage);
			outstationDevice.getDatabaseManager().setInternalIndicatorDataPoint(dataPoint);
		} catch (Exception e) {
			LOGGER.error("Failed to copy object.", e);
		}
	}
}
