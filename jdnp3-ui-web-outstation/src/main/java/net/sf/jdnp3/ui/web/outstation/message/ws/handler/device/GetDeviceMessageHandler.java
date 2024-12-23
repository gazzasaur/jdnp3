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

import java.util.ArrayList;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceMessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.DoubleBitBinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.counter.CounterMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.device.GetDeviceMessage;

public class GetDeviceMessageHandler implements DeviceMessageHandler, MessageHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GetDeviceMessageHandler.class);
	
	public boolean canHandle(Message message) {
		return message instanceof GetDeviceMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		GetDeviceMessage specificMessage = (GetDeviceMessage) message;

		// FIXME NPE Check
		OutstationDevice outstation = DeviceProvider.getDevice(specificMessage.getSite(), specificMessage.getDevice());
		this.processMessage(messanger, outstation, specificMessage);
	}

	public void processMessage(Messanger messanger, OutstationDevice outstationDevice, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		GetDeviceMessage specificMessage = (GetDeviceMessage) message;
		specificMessage.setBinaryInputPoints(new ArrayList<>());

		try {
			DatabaseManager databaseManager = outstationDevice.getDatabaseManager();
			InternalIndicatorsDataPoint internalIndicatorsDataPoint = databaseManager.getInternalIndicatorsDataPoint();
			specificMessage.setInternalIndicators(internalIndicatorsDataPoint);
			
			for (BinaryInputDataPoint point : databaseManager.getBinaryInputDataPoints()) {
				BinaryInputMessage part = new BinaryInputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getBinaryInputPoints().add(part);
			}
			for (DoubleBitBinaryInputDataPoint point : databaseManager.getDoubleBitBinaryInputDataPoints()) {
				DoubleBitBinaryInputMessage part = new DoubleBitBinaryInputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getDoubleBitBinaryInputPoints().add(part);
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
			for (AnalogOutputDataPoint point : databaseManager.getAnalogOutputDataPoints()) {
				AnalogOutputMessage part = new AnalogOutputMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getAnalogOutputPoints().add(part);
			}
			for (CounterDataPoint point : databaseManager.getCounterDataPoints()) {
				CounterMessage part = new CounterMessage();
				BeanUtils.copyProperties(part, point);
				specificMessage.getCounterPoints().add(part);
			}
			specificMessage.getOutstationBindings().addAll(DataLinkManagerProvider.getDataLinkBindings(outstationDevice));
		} catch (Exception e) {
			LOGGER.error("Failed to copy object.", e);
		}
		
		messanger.sendMessage(specificMessage);
	}
}
