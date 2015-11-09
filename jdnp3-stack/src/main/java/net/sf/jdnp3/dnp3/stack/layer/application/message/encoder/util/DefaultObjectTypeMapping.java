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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_FLOAT64_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_STATIC_FLOAT64;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_STATIC_PACKED;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_COMMAND_CROB;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_STATIC_PACKED;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputCrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class DefaultObjectTypeMapping {
	@SuppressWarnings("serial")
	private Map<Class<? extends ObjectInstance>, ObjectType> mappings = new HashMap<Class<? extends ObjectInstance>, ObjectType>() {{
		this.put(BinaryInputStaticObjectInstance.class, BINARY_INPUT_STATIC_PACKED);
		this.put(BinaryInputEventObjectInstance.class, BINARY_INPUT_EVENT_ABSOLUTE_TIME);
		this.put(BinaryOutputStaticObjectInstance.class, BINARY_OUTPUT_STATIC_PACKED);
		this.put(BinaryOutputCrobObjectInstance.class, BINARY_OUTPUT_COMMAND_CROB);
		this.put(AnalogInputStaticObjectInstance.class, ANALOG_INPUT_STATIC_FLOAT64);
		this.put(AnalogInputEventObjectInstance.class, ANALOG_INPUT_EVENT_FLOAT64_ABSOLUTE_TIME);
	}};
	
	public void performMapping(ObjectInstance objectInstance) {
		ObjectType defaultObjectType = mappings.get(objectInstance.getClass());
		if (defaultObjectType == null) {
			return;
		}
		
		if (objectInstance.getRequestedType().getGroup() != defaultObjectType.getGroup() || objectInstance.getRequestedType().getVariation() == 0) {
			objectInstance.setRequestedType(defaultObjectType);
		}
	}

	public void addMapping(Class<? extends ObjectInstance> clazz, ObjectType defaultMapping) {
		mappings.put(clazz, defaultMapping);
	}
}
