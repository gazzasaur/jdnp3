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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_RELATIVE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_1;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_2;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_3;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class BinaryInputEventTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryInputEventTypeRationaliserHelper.class);
	private List<ObjectType> groupObjectTypes = Arrays.asList(ANY, CLASS_1, CLASS_2, CLASS_3, BINARY_INPUT_EVENT_ANY);
	private List<ObjectType> validObjectTypes = Arrays.asList(ANY, CLASS_1, CLASS_2, CLASS_3, BINARY_INPUT_EVENT_ANY, BINARY_INPUT_EVENT_ABSOLUTE_TIME, BINARY_INPUT_EVENT_RELATIVE_TIME, BINARY_INPUT_EVENT_WITHOUT_TIME);
	
	public void rationalise(ObjectInstance objectInstance) {
		BinaryInputEventObjectInstance specificInstance = (BinaryInputEventObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			LOGGER.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		if (groupObjectTypes.contains(objectInstance.getRequestedType())) {
			objectInstance.setRequestedType(BINARY_INPUT_EVENT_ABSOLUTE_TIME);
		}
	}
}
