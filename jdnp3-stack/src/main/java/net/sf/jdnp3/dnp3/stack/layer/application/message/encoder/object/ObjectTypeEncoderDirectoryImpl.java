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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_STATIC_FLAGS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_STATIC_PACKED;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;

public class ObjectTypeEncoderDirectoryImpl implements ObjectTypeEncoderDirectory {
	@SuppressWarnings("serial")
	private Map<ObjectType, ObjectTypeEncoder> objectTypeEncoders = new HashMap<ObjectType, ObjectTypeEncoder>() {{
		this.put(BINARY_INPUT_STATIC_PACKED, new BinaryInputStaticPackedObjectTypeEncoder());
		this.put(BINARY_INPUT_STATIC_FLAGS, new BinaryInputStaticFlagsObjectTypeEncoder());
		
		this.put(ObjectTypeConstants.BINARY_INPUT_EVENT_ABSOLUTE_TIME, new BinaryInputEventAbsoluteTimeObjectTypeEncoder());
	}};

	public ObjectTypeEncoder getObjectTypeEncoder(ObjectInstance objectInstance) {
		ObjectTypeEncoder objectTypeEncoder = objectTypeEncoders.get(objectInstance.getRequestedType());
		if (objectTypeEncoder == null) {
			throw new IllegalArgumentException(String.format("No object type encoder could be found for the type %s.", objectInstance.getRequestedType()));
		}
		return objectTypeEncoder;
	}
}
