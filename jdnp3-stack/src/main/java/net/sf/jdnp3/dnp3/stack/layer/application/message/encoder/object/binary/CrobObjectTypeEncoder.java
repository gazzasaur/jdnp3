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
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CROB;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectFragmentHeaderEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.QualifierFieldCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.CrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class CrobObjectTypeEncoder implements ObjectTypeEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();

	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(DIRECT_OPERATE) && objectType.equals(CROB);
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
		QualifierField qualifierField = QualifierFieldCalculator.calculate(indexPrefixType, countRange);
		
		objectFragmentHeaderEncoder.encode(context.getObjectType(), qualifierField, countRange, data);
		
		for (ObjectInstance objectInstance : objectInstances) {
			CrobObjectInstance specificInstance = (CrobObjectInstance) objectInstance;
			DataUtils.addInteger(objectInstance.getIndex(), qualifierField.getObjectPrefixCode().getOctetCount(), data);
			byte firstByte = (byte) ((specificInstance.getTripCloseCode().getCode() << 6) & 0xC0);
			firstByte |= (byte) (specificInstance.getOperationType().getCode() & 0x0F);
			
			DataUtils.addInteger(firstByte, 1, data);
			DataUtils.addInteger(specificInstance.getCount(), 1, data);
			DataUtils.addInteger(specificInstance.getOnTime(), 4, data);
			DataUtils.addInteger(specificInstance.getOffTime(), 4, data);
			DataUtils.addInteger(specificInstance.getStatusCode().getCode(), 1, data);
		}
	}
}
