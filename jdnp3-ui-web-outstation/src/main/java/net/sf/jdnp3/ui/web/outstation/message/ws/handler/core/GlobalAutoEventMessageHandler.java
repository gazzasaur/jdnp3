/**
 * Copyright 2025 Graeme Farquharson
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

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.StringUtils;

import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.main.SiteDeviceList;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.GlobalAutoEventMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class GlobalAutoEventMessageHandler implements MessageHandler {
	private static final Map<String, BiConsumer<OutstationDevice, Boolean>> processors = new HashMap<String, BiConsumer<OutstationDevice, Boolean>>() {{
		put("binaryInputPoint", (OutstationDevice device, Boolean enable) -> {
			for (BinaryInputDataPoint dataPoint : device.getDatabaseManager().getBinaryInputDataPoints()) {
				dataPoint.setTriggerEventOnChange(enable);
				device.getDatabaseManager().setBinaryInputDataPoint(dataPoint, true);
			}
		});

		put("doubleBitBinaryInputPoint", (OutstationDevice device, Boolean enable) -> {
			for (DoubleBitBinaryInputDataPoint dataPoint : device.getDatabaseManager().getDoubleBitBinaryInputDataPoints()) {
				dataPoint.setTriggerEventOnChange(enable);
				device.getDatabaseManager().setDoubleBitBinaryInputDataPoint(dataPoint, true);
			}
		});

		put("analogInputPoint", (OutstationDevice device, Boolean enable) -> {
			for (AnalogInputDataPoint dataPoint : device.getDatabaseManager().getAnalogInputDataPoints()) {
				dataPoint.setTriggerEventOnChange(enable);
				device.getDatabaseManager().setAnalogInputDataPoint(dataPoint, true);
			}
		});

		put("counterPoint", (OutstationDevice device, Boolean enable) -> {
			for (CounterDataPoint dataPoint : device.getDatabaseManager().getCounterDataPoints()) {
				dataPoint.setTriggerEventOnChange(enable);
				device.getDatabaseManager().setCounterDataPoint(dataPoint, true);
			}
		});
	}};

	public boolean canHandle(Message message) {
		return message instanceof GlobalAutoEventMessage;
	}

	public void processMessage(Messanger messanger, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}

		GlobalAutoEventMessage globalAutoEventMessage = ((GlobalAutoEventMessage) message);

		for (SiteDeviceList site : DeviceProvider.gettDeviceList().getSiteDeviceLists()) {
			for (String deviceName : site.getDevices()) {
				OutstationDevice device = DeviceProvider.getDevice(site.getSite(), deviceName);

				processors.entrySet().forEach(entry -> {
					if (StringUtils.isBlank(globalAutoEventMessage.getPointType()) || entry.getKey().equals(globalAutoEventMessage.getPointType())) {
						entry.getValue().accept(device, globalAutoEventMessage.getEnable());
					}
				});
			}
		}
		return;
	}
}
