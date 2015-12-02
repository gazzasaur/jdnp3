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

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.BindDeviceMessage;

public class BindDeviceMessageHandler implements MessageHandler {
	public boolean canHandle(Message message) {
		return message instanceof BindDeviceMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		BindDeviceMessage specificMessage = (BindDeviceMessage) message;
		OutstationDevice outstationDevice = DeviceProvider.getDevice(specificMessage.getSite(), specificMessage.getDevice());
		DataLinkManager dataLinkManager = DataLinkManagerProvider.getDataLinkManager(specificMessage.getDataLinkName());
		dataLinkManager.bind(specificMessage.getAddress(), outstationDevice);
	}
}
