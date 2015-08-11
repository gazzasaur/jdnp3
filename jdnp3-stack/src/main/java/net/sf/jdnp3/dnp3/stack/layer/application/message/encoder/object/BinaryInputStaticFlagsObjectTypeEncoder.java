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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_STATIC_FLAGS;

import java.util.BitSet;
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

public class BinaryInputStaticFlagsObjectTypeEncoder implements ObjectTypeEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();

	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(FunctionCode.RESPONSE) && objectType.equals(BINARY_INPUT_STATIC_FLAGS);
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
		
		for (ObjectInstance objectInstance : objectInstances) {
			BinaryInputStaticObjectInstance specificInstance = (BinaryInputStaticObjectInstance) objectInstance;
			BitSet bitSet = new BitSet(8);
			bitSet.set(7, specificInstance.isActive());
			bitSet.set(5, specificInstance.isChatterFilter());
			bitSet.set(4, specificInstance.isLocalForced());
			bitSet.set(3, specificInstance.isRemoteForced());
			bitSet.set(2, specificInstance.isCommunicationsLost());
			bitSet.set(1, specificInstance.isRestart());
			bitSet.set(0, specificInstance.isOnline());
			
			byte[] rawValue = bitSet.toByteArray();
			byte value = 0;
			if (rawValue.length > 0) {
				value = rawValue[0];
			}
			
			data.add(value);
		}
	}
}
