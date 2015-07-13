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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class BinaryInputStaticPackedObjectTypeEncoder implements ObjectTypeEncoder {
	public ObjectPrefixCode calculateObjectPrefix() {
		return ObjectPrefixCode.NONE;
	}
	
	public Range calculateRangeType(long count, long startPrefix, long stopPrefix, ObjectInstance lastObjectInstance) {
		IndexRange range = new IndexRange();
		range.setStartIndex(startPrefix);
		range.setStopIndex(stopPrefix);
		return range;
	}

	public boolean fragment(ObjectInstance currentObjectInstance, ObjectInstance previousObjectInstance) {
		return !currentObjectInstance.getRequestedType().equals(previousObjectInstance.getRequestedType()) || (currentObjectInstance.getIndex() - 1) != previousObjectInstance.getIndex();
	}
	
	public void encode(long startPrefix, ObjectField objectField, List<Byte> data) {
		BinaryInputStaticObjectInstance binary = (BinaryInputStaticObjectInstance) objectField.getObjectInstance();
		
		if ((objectField.getPrefix() - startPrefix) % 8 == 0) {
			data.add((byte) 0);
		}
		byte value = data.get(data.size() - 1);
		value |= ((binary.isActive()) ? 1 : 0) << ((objectField.getPrefix() - startPrefix) % 8);
		data.set(data.size() - 1, value);
	}
}
