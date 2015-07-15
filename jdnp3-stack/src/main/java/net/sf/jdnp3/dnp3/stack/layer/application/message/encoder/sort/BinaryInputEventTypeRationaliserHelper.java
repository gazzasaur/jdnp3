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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.sort;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_RELATIVE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_WITHOUT_TIME;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryInputEventTypeRationaliserHelper implements ObjectInstanceTypeRationaliserHelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	private List<ObjectType> validObjectTypes = Arrays.asList(ANY, BINARY_INPUT_EVENT_ANY, BINARY_INPUT_EVENT_WITHOUT_TIME, BINARY_INPUT_EVENT_ABSOLUTE_TIME, BINARY_INPUT_EVENT_RELATIVE_TIME);
	
	public void rationalise(ObjectInstance objectInstance) {
		BinaryInputEventObjectInstance specificInstance = (BinaryInputEventObjectInstance) objectInstance;
		if (!validObjectTypes.contains(specificInstance.getRequestedType())) {
			logger.warn(format("Unknown object type '%s' for class '%s', setting to ANY.", specificInstance.getRequestedType(), specificInstance.getClass()));
			objectInstance.setRequestedType(ANY);
		}
		if (specificInstance.getRequestedType().equals(ANY) || specificInstance.getRequestedType().equals(BINARY_INPUT_EVENT_ANY)) {
			objectInstance.setRequestedType(BINARY_INPUT_EVENT_ABSOLUTE_TIME);
		}
	}
}
