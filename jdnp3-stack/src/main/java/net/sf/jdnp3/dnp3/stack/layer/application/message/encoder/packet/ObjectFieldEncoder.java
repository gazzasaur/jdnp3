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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.BinaryInputStaticObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class ObjectFieldEncoder {
	@SuppressWarnings("serial")
	private Map<Class<? extends ObjectInstance>, ObjectTypeEncoder> objectTypeEncoders = new HashMap<Class<? extends ObjectInstance>, ObjectTypeEncoder>() {{
		this.put(BinaryInputStaticObjectInstance.class, new BinaryInputStaticObjectTypeEncoder());
	}};
	
	public void encode(long startPrefix, ObjectPrefixCode objectPrefixCode, ObjectField objectField, List<Byte> data) {
		if (objectPrefixCode.getOctetCount() > 0) {
			DataUtils.addInteger(objectField.getPrefix(), objectPrefixCode.getOctetCount(), data);
		}
		ObjectTypeEncoder encoder = objectTypeEncoders.get(objectField.getObjectInstance().getClass());
		
		if (encoder == null) {
			throw new IllegalArgumentException("Cannot encode object of type: " + objectField.getObjectInstance().getClass());
		}
		encoder.encode(startPrefix, objectField, data);
	}
}
