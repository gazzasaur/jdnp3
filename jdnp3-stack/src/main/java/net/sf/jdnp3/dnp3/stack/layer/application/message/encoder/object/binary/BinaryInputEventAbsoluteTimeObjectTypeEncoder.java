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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_ABSOLUTE_TIME;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.EncoderUtils;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectFragmentHeaderEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class BinaryInputEventAbsoluteTimeObjectTypeEncoder implements ObjectTypeEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();

	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(FunctionCode.RESPONSE) && objectType.equals(BINARY_INPUT_EVENT_ABSOLUTE_TIME);
	}

	public void encode(ObjectFragmentEncoderContext context, List<ObjectInstance> objectInstances, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType()) || objectInstances.size() < 1) {
			throw new IllegalArgumentException(format("Cannot encode the give value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}
		CountRange countRange = new CountRange();
		countRange.setCount(0);
		
		long maxIndex = 0;
		for (ObjectInstance objectInstance : objectInstances) {
			if (objectInstance.getIndex() > maxIndex) {
				maxIndex = objectInstance.getIndex();
			}
			countRange.setCount(countRange.getCount() + 1);
		}
		
		IndexPrefixType indexPrefixType = new IndexPrefixType();
		indexPrefixType.setOctetCount(EncoderUtils.calculateOctetCount(maxIndex));
		QualifierField qualifierField = QualifierFieldCalculator.calculate(indexPrefixType, countRange);
		
		objectFragmentHeaderEncoder.encode(context.getObjectType(), qualifierField, countRange, data);
		
		for (ObjectInstance objectInstance : objectInstances) {
			BinaryInputEventObjectInstance specificInstance = (BinaryInputEventObjectInstance) objectInstance;
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
			
			DataUtils.addInteger(specificInstance.getIndex(), qualifierField.getObjectPrefixCode().getOctetCount(), data);
			data.add(value);
			DataUtils.addInteger(specificInstance.getTimestamp(), 6, data);
		}
	}
}
