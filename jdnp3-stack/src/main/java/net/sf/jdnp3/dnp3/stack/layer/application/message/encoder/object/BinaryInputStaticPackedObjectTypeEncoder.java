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

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_STATIC_PACKED;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class BinaryInputStaticPackedObjectTypeEncoder implements ObjectTypeEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();
	
	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(FunctionCode.RESPONSE) && objectType.equals(BINARY_INPUT_STATIC_PACKED);
	}

	public void encode(ObjectFragmentEncoderContext context, List<ObjectInstance> objectInstances, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType()) || objectInstances.size() < 1) {
			throw new IllegalArgumentException(format("Cannot encode the give value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}
		IndexRange indexRange = new IndexRange();
		indexRange.setStartIndex(objectInstances.get(0).getIndex());
		indexRange.setStopIndex(objectInstances.get(objectInstances.size() - 1).getIndex());
		
		QualifierField qualifierField = QualifierFieldCalculator.calculate(new NoPrefixType(), indexRange);
		objectFragmentHeaderEncoder.encode(context.getObjectType(), qualifierField, indexRange, data);
		
		int index = 0;
		for (ObjectInstance objectInstance : objectInstances) {
			BinaryInputStaticObjectInstance specificObjectInstance = (BinaryInputStaticObjectInstance) objectInstance;
			if (index % 8 == 0) {
				data.add((byte) 0);
			}
			
			if (specificObjectInstance.isActive()) {
				data.set(data.size() - 1, (byte) (data.get(data.size() - 1) | (1 << (index % 8))));
			}
			++index;
		}
	}
}
