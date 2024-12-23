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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler.core;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DestroyAllMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.FailureMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SuccessMessage;

public class DestroyAllMessageHandler implements MessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DestroyAllMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof DestroyAllMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		Message responseMessage = new SuccessMessage();

		List<String> stationNames = DeviceProvider.getStationNames();
		for (String stationName : stationNames) {
			try {
				List<String> deviceNames = DeviceProvider.getDeviceNames(stationName);
				for (String deviceName : deviceNames) {
					try {
						DeviceProvider.unregisterDevice(stationName, deviceName);
					} catch (Exception e) {
						LOGGER.warn("Cannot destroy device {}:{}.", stationName, deviceName, e);
						FailureMessage failureMessage = new FailureMessage();
						failureMessage.setReason("Failed to destroy all devices.  Please check log for details.");
						responseMessage = failureMessage;
					}
				}
			} catch (Exception e) {
				LOGGER.warn("Cannot destroy station {}.", stationName, e);
				FailureMessage failureMessage = new FailureMessage();
				failureMessage.setReason("Failed to destroy all devices.  Please check log for details.");
				responseMessage = failureMessage;
			}
		}

		List<String> dataLinkNames = DataLinkManagerProvider.getDataLinkNames();
		for (String name : dataLinkNames) {
			try {
				DataLinkManagerProvider.unregisterDataLink(name);
			} catch (Exception e) {
				LOGGER.warn("Cannot destroy data link: " + name, e);
				FailureMessage failureMessage = new FailureMessage();
				failureMessage.setReason("Failed to destroy all devices.  Please check log for details.");
				responseMessage = failureMessage;
			}
		}

		messanger.sendMessage(responseMessage);
	}
}
