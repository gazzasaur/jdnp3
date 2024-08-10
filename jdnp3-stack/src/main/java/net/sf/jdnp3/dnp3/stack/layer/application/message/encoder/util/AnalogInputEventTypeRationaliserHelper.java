/**
 * Copyright 2024 Graeme Farquharson
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

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_FLOAT32_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_FLOAT32_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_FLOAT64_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_FLOAT64_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_INT16_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_INT16_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_INT32_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_INPUT_EVENT_INT32_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_1;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_2;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_3;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

// FIXME Rationalisers should only be required for fixing things like flags. Use the DefaultObjectTypeMappings for everything else.
public class AnalogInputEventTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<ObjectType> groupObjectTypes = Arrays.asList(
		ANY,
		CLASS_1,
		CLASS_2,
		CLASS_3,
		ANALOG_INPUT_EVENT_ANY
	);
	private List<ObjectType> validObjectTypes = Arrays.asList(
		ANY,
		CLASS_1,
		CLASS_2,
		CLASS_3,
		ANALOG_INPUT_EVENT_ANY,
		ANALOG_INPUT_EVENT_INT16_WITHOUT_TIME,
		ANALOG_INPUT_EVENT_INT16_ABSOLUTE_TIME,
		ANALOG_INPUT_EVENT_INT32_WITHOUT_TIME,
		ANALOG_INPUT_EVENT_INT32_ABSOLUTE_TIME,
		ANALOG_INPUT_EVENT_FLOAT32_WITHOUT_TIME,
		ANALOG_INPUT_EVENT_FLOAT32_ABSOLUTE_TIME,
		ANALOG_INPUT_EVENT_FLOAT64_WITHOUT_TIME,
		ANALOG_INPUT_EVENT_FLOAT64_ABSOLUTE_TIME
	);
	
	public void rationalise(ObjectInstance objectInstance) {
		AnalogInputEventObjectInstance specificInstance = (AnalogInputEventObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			logger.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		if (groupObjectTypes.contains(specificInstance.getRequestedType())) {
			objectInstance.setRequestedType(ANALOG_INPUT_EVENT_FLOAT32_WITHOUT_TIME);
		}
	}
}
