/**
 * Copyright 2016 Graeme Farquharson
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
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectTypeEncoderConstants.OBJECT_TYPE_ENCODERS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util.EncoderUtils.calculateOctetCount;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class CountRangeObjectFragmentPacker implements ObjectFragmentPacker {
	public boolean canPack(Class<? extends ObjectInstance> clazz) {
		return EventObjectInstance.class.isAssignableFrom(clazz);
	}
	
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

		// FIXME This should be calculated from the max intexd that is included on the object.
		long maxIndex = objectInstances.stream().map(ObjectInstance::getIndex).mapToLong(index -> index).max().orElse(1);
		int indexSize = calculateOctetCount(maxIndex);

		CountRange countRange = new CountRange();
		CountRange committedCountRange = new CountRange();

		IndexPrefixType indexPrefixType = new IndexPrefixType();
		indexPrefixType.setOctetCount(indexSize);
		ObjectFragment objectFragment = new ObjectFragment();
		objectFragment.getObjectFragmentHeader().setRange(committedCountRange);
		objectFragment.getObjectFragmentHeader().setObjectType(objectType);
		objectFragment.getObjectFragmentHeader().setPrefixType(indexPrefixType);
		
		ObjectFragmentEncoderContext encoderContext = new ObjectFragmentEncoderContext();
		encoderContext.setCommonTimeOfOccurrance(context.getTimeReference());
		encoderContext.setFunctionCode(context.getFunctionCode());
		encoderContext.setObjectType(objectType);

		long committedObjectSize = 0;
		long committedMaxCount = 0;
		long addedObjects = 0;
		long overhead = 2;
		
		List<Byte> data = new ArrayList<Byte>();
		
		while (objectInstances.size() > 0) {
			ObjectInstance nextInstance = objectInstances.get(0);
			if (!nextInstance.getRequestedType().equals(objectType)) {
				break;
			}
						
			countRange.setCount(countRange.getCount() + 1);
			long rangeSize = calculateOctetCount(countRange.getCount());
			
			encoderContext.setCurrentIndex(nextInstance.getIndex());
			objectTypeEncoder.encode(encoderContext, nextInstance, data);
			long objectSize = data.size();
			if (overhead + rangeSize + (countRange.getCount() * indexSize) + objectSize < context.getFreeSpace()) {
				objectInstances.remove(0);
				addedObjects += 1;
				committedMaxCount += 1;
				committedObjectSize = objectSize;
				objectFragment.addObjectInstance(nextInstance);
				committedCountRange.setCount(committedCountRange.getCount() + 1);
			} else {
				result.setAtCapacity(true);
				break;
			}
		}

		if (addedObjects == 0) {
			return result;
		}
		
		QualifierField qualifierField = QualifierFieldCalculator.calculate(indexPrefixType, countRange);
		objectFragment.getObjectFragmentHeader().setQualifierField(qualifierField);
		context.setFreeSpace(context.getFreeSpace() - (overhead + calculateOctetCount(committedMaxCount) + (committedMaxCount * indexSize) + committedObjectSize));
		
		result.setObjectFragment(objectFragment);
		return result;
	}
}
