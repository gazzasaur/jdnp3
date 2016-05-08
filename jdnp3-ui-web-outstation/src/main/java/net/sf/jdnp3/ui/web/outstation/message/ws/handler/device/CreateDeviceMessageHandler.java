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

import org.apache.commons.lang3.ObjectUtils;

import net.sf.jdnp3.ui.web.outstation.main.DeviceFactory;
import net.sf.jdnp3.ui.web.outstation.main.DeviceFactoryRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.CreateDeviceMessage;

public class CreateDeviceMessageHandler implements MessageHandler {
	public boolean canHandle(Message message) {
		return message instanceof CreateDeviceMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		CreateDeviceMessage specificMessage = (CreateDeviceMessage) message;
		
		if (ObjectUtils.defaultIfNull(specificMessage.getSite(), "").isEmpty() || ObjectUtils.defaultIfNull(specificMessage.getSite(), "").isEmpty()) {
			throw new IllegalArgumentException("A siteCode and deviceCode must be specified.");
		}
		
		DeviceFactory deviceFactory = DeviceFactoryRegistry.getFactory(specificMessage.getDeviceFactory());
		
		String site = specificMessage.getSite();
		String device = specificMessage.getDevice();
		deviceFactory.create(site, device, specificMessage.getPrimaryAddress(), specificMessage.getExtendedConfiguration());
	}
}
