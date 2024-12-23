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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.RESPONSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_COMMAND_CROB;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.addInteger;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCodeUtils;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputCrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class CrobObjectTypeEncoder implements ObjectTypeEncoder {
	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return FunctionCodeUtils.isResponseCode(functionCode) && objectType.equals(BINARY_OUTPUT_COMMAND_CROB);
	}

	public void encode(ObjectFragmentEncoderContext context, ObjectInstance objectInstance, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType())) {
			throw new IllegalArgumentException(format("Cannot encode the give value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}

		BinaryOutputCrobObjectInstance specificInstance = (BinaryOutputCrobObjectInstance) objectInstance;
		byte firstByte = (byte) ((specificInstance.getTripCloseCode().getCode() << 6) & 0xC0);
		firstByte |= (byte) (specificInstance.getOperationType().getCode() & 0x0F);
			
		addInteger(firstByte, 1, data);
		addInteger(specificInstance.getCount(), 1, data);
		addInteger(specificInstance.getOnTime(), 4, data);
		addInteger(specificInstance.getOffTime(), 4, data);
		addInteger(specificInstance.getStatusCode().getCode(), 1, data);
	}
}
