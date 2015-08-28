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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.EncoderUtils.calculateOctetCount;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectTypeEncoderConstants.OBJECT_TYPE_ENCODERS;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class SingleObjectFragmentPacker implements ObjectFragmentPacker {
	public ObjectFragmentPackerResult pack(ObjectFragmentPackerContext context, List<ObjectInstance> objectInstances) {
		if (objectInstances.size() < 1) {
			throw new IllegalArgumentException("Cannot create an object fragment of size 0.");
		}
		
		ObjectFragmentPackerResult result = new ObjectFragmentPackerResult();
		
		ObjectInstance firstInstance = objectInstances.get(0);
		ObjectType objectType = firstInstance.getRequestedType();
		ObjectTypeEncoder objectTypeEncoder = null;
		for (ObjectTypeEncoder encoder : OBJECT_TYPE_ENCODERS) {
			if (encoder.canEncode(context.getFunctionCode(), objectType)) {
				objectTypeEncoder = encoder;
			}
		}
		if (objectTypeEncoder == null) {
			throw new IllegalArgumentException(format("No encoder found for the operation %s on type %s.", context.getFunctionCode(), objectType));
		}
		
		CountRange countRange = new CountRange();
		NoPrefixType indexPrefixType = new NoPrefixType();
		ObjectFragment objectFragment = new ObjectFragment();
		objectFragment.getObjectFragmentHeader().setRange(countRange);
		objectFragment.getObjectFragmentHeader().setObjectType(objectType);
		objectFragment.getObjectFragmentHeader().setPrefixType(indexPrefixType);
		
		ObjectFragmentEncoderContext encoderContext = new ObjectFragmentEncoderContext();
		encoderContext.setCommonTimeOfOccurrance(context.getTimeReference());
		encoderContext.setFunctionCode(context.getFunctionCode());
		encoderContext.setObjectType(objectType);
		long overhead = 2;
		
		List<Byte> data = new ArrayList<Byte>();
		
		countRange.setCount(1);
		long rangeSize = calculateOctetCount(countRange.getCount());
			
		encoderContext.setCurrentIndex(firstInstance.getIndex());
		objectTypeEncoder.encode(encoderContext, firstInstance, data);
		long objectSize = data.size();
		if (overhead + rangeSize + objectSize < context.getFreeSpace()) {
			objectInstances.remove(0);
			objectFragment.addObjectInstance(firstInstance);
		} else {
			result.setAtCapacity(true);
		}
		
		QualifierField qualifierField = QualifierFieldCalculator.calculate(indexPrefixType, countRange);
		objectFragment.getObjectFragmentHeader().setQualifierField(qualifierField);
		
		result.setObjectFragment(objectFragment);
		return result;
	}
}
