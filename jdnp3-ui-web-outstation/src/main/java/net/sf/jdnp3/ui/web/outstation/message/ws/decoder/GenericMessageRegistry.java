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
package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class GenericMessageRegistry {
	private Map<String, Class<? extends Message>> typeMessageMap = new HashMap<>();
	private Map<Class<? extends DataPoint>, Class<? extends Message>> dataPointMessageMap = new HashMap<>();
	
	public void register(String type, Class<? extends Message> messageClass) {
		typeMessageMap.put(type, messageClass);
	}
	
	public void register(String type, Class<? extends DataPoint> dataPointClass, Class<? extends Message> messageClass) {
		typeMessageMap.put(type, messageClass);
		dataPointMessageMap.put(dataPointClass, messageClass);
	}
	
	public boolean isRegistered(String type) {
		return typeMessageMap.containsKey(type);
	}
	
	public boolean isRegistered(Class<? extends DataPoint> dataPointClass) {
		return dataPointMessageMap.containsKey(dataPointClass);
	}

	public Class<? extends Message> get(String type) {
		Class<? extends Message> clazz = typeMessageMap.get(type);
		if (clazz == null) {
			throw new IllegalArgumentException(format("The type %s is not registered.", type));
		}
		return clazz;
	}

	public Class<? extends Message> get(Class<? extends DataPoint> dataPointClass) {
		Class<? extends Message> clazz = dataPointMessageMap.get(dataPointClass);
		if (clazz == null) {
			throw new IllegalArgumentException(format("The class %s is not registered.", dataPointClass));
		}
		return clazz;
	}
}
