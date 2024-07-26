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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_FLOAT32;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_FLOAT64;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_INT16;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_INT32;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_0;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class AnalogOutputStaticTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<ObjectType> validObjectTypes = Arrays.asList(ANY, CLASS_0, ANALOG_OUTPUT_STATIC_ANY, ANALOG_OUTPUT_STATIC_INT32, ANALOG_OUTPUT_STATIC_INT16, ANALOG_OUTPUT_STATIC_FLOAT32, ANALOG_OUTPUT_STATIC_FLOAT64);
	
	public void rationalise(ObjectInstance objectInstance) {
		AnalogOutputStaticObjectInstance specificInstance = (AnalogOutputStaticObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			logger.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		if (specificInstance.getRequestedType().getGroup() != ANALOG_OUTPUT_STATIC_ANY.getGroup() || specificInstance.getRequestedType().getVariation() == 0) {
			objectInstance.setRequestedType(ANALOG_OUTPUT_STATIC_FLOAT32);
		}
	}
}
