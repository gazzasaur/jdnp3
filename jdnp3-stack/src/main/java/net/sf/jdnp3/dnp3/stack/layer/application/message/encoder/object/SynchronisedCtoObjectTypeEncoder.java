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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.SYNCHRONISED_CTO;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.SynchronisedCtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class SynchronisedCtoObjectTypeEncoder implements ObjectTypeEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();

	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(FunctionCode.RESPONSE) && objectType.equals(SYNCHRONISED_CTO);
	}

	public void encode(ObjectFragmentEncoderContext context, List<ObjectInstance> objectInstances, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType()) || objectInstances.size() < 1) {
			throw new IllegalArgumentException(format("Cannot encode the give value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}
		CountRange countRange = new CountRange();
		countRange.setCount(1);
		
		NoPrefixType noPrefixType = new NoPrefixType();
		QualifierField qualifierField = QualifierFieldCalculator.calculate(noPrefixType, countRange);
		
		objectFragmentHeaderEncoder.encode(context.getObjectType(), qualifierField, countRange, data);
		
		SynchronisedCtoObjectInstance synchronisedCtoObjectInstance = (SynchronisedCtoObjectInstance)objectInstances.get(0);
		context.setCommonTimeOfOccurrance(synchronisedCtoObjectInstance.getTimestamp());
		DataUtils.addInteger(synchronisedCtoObjectInstance.getTimestamp(), 6, data);
	}
}
