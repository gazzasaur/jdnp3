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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NullRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class CustomSingleObjectFragmentPacker implements ObjectFragmentPacker {
	private int expectedSize;
	private Class<? extends ObjectInstance> targetClass;

	public CustomSingleObjectFragmentPacker(Class<? extends ObjectInstance> targetClass, int expectedSize) {
		this.targetClass = targetClass;
		this.expectedSize = expectedSize;
	}
	
	public boolean canPack(Class<? extends ObjectInstance> clazz) {
		return targetClass.isAssignableFrom(clazz);
	}
	
	public ObjectFragmentPackerResult pack(ObjectFragmentPackerContext context, List<ObjectInstance> objectInstances) {
		if (objectInstances.size() < 1) {
			throw new IllegalArgumentException("Cannot create an object fragment of size 0.");
		}
		
		ObjectFragmentPackerResult result = new ObjectFragmentPackerResult();
		
		ObjectInstance firstInstance = objectInstances.get(0);
		ObjectType objectType = firstInstance.getRequestedType();
		
		NullRange nullRange = new NullRange();
		NoPrefixType noPrefixType = new NoPrefixType();
		ObjectFragment objectFragment = new ObjectFragment();
		objectFragment.getObjectFragmentHeader().setRange(nullRange);
		objectFragment.getObjectFragmentHeader().setObjectType(objectType);
		objectFragment.getObjectFragmentHeader().setPrefixType(noPrefixType);
		
		ObjectFragmentEncoderContext encoderContext = new ObjectFragmentEncoderContext();
		encoderContext.setCommonTimeOfOccurrance(context.getTimeReference());
		encoderContext.setFunctionCode(context.getFunctionCode());
		encoderContext.setObjectType(objectType);
		
		if (expectedSize < context.getFreeSpace()) {
			objectInstances.remove(0);
			objectFragment.addObjectInstance(firstInstance);
		} else {
			result.setAtCapacity(true);
		}
		
		QualifierField qualifierField = QualifierFieldCalculator.calculate(noPrefixType, nullRange);
		objectFragment.getObjectFragmentHeader().setQualifierField(qualifierField);
		
		result.setObjectFragment(objectFragment);
		return result;
	}
}
