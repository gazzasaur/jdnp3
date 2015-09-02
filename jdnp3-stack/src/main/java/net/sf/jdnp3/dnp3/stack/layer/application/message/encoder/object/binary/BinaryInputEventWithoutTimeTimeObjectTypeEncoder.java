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
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_WITHOUT_TIME;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.bitSetToByte;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class BinaryInputEventWithoutTimeTimeObjectTypeEncoder implements ObjectTypeEncoder {
	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return functionCode.equals(FunctionCode.RESPONSE) && objectType.equals(BINARY_INPUT_EVENT_WITHOUT_TIME);
	}

	public void encode(ObjectFragmentEncoderContext context, ObjectInstance objectInstance, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType())) {
			throw new IllegalArgumentException(format("Cannot encode the given value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}

		BinaryInputEventObjectInstance specificInstance = (BinaryInputEventObjectInstance) objectInstance;
		BitSet bitSet = new BitSet(8);
		bitSet.set(7, specificInstance.isActive());
		bitSet.set(5, specificInstance.isChatterFilter());
		bitSet.set(4, specificInstance.isLocalForced());
		bitSet.set(3, specificInstance.isRemoteForced());
		bitSet.set(2, specificInstance.isCommunicationsLost());
		bitSet.set(1, specificInstance.isRestart());
		bitSet.set(0, specificInstance.isOnline());
			
		data.add(bitSetToByte(bitSet));
	}
}
