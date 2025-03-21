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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageUtils;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.counter.CounterMessage;

public class CounterMessageHandler implements DeviceMessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(CounterMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof CounterMessage;
	}

	public void processMessage(Messanger messanger, OutstationDevice outstationDevice, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		CounterMessage pointMessage = (CounterMessage) message;

		try {
			CounterDataPoint dataPoint = outstationDevice.getDatabaseManager().getCounterDataPoint(pointMessage.getIndex());
			MessageUtils.copyProperties(dataPoint, pointMessage);
			outstationDevice.getDatabaseManager().setCounterDataPoint(dataPoint);
		} catch (Exception e) {
			LOGGER.error("Failed to copy object.", e);
		}
	}
}
