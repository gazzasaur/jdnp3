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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.binary;

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_STATIC_FLAGS;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.binary.BinaryFlagsEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class BinaryInputStaticFlagsObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canEncode(FunctionCode functionCode, ObjectType objectType) {
		return objectType.equals(BINARY_INPUT_STATIC_FLAGS);
	}

	public void encode(ObjectFragmentEncoderContext context, ObjectInstance objectInstance, List<Byte> data) {
		if (!this.canEncode(context.getFunctionCode(), context.getObjectType())) {
			throw new IllegalArgumentException(format("Cannot decode the give value %s %s.", context.getFunctionCode(), context.getObjectType()));
		}

		BinaryInputStaticObjectInstance specificInstance = (BinaryInputStaticObjectInstance) objectInstance;
		data.add(BinaryFlagsEncoder.encode(specificInstance));
	}

	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(BINARY_INPUT_STATIC_FLAGS);
	}

	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		BinaryInputStaticObjectInstance objectInstance = new BinaryInputStaticObjectInstance();
		objectInstance.setIndex(decoderContext.getCurrentIndex());
		objectInstance.setRequestedType(decoderContext.getObjectType());
		
		BitSet bitSet = BitSet.valueOf(new byte[] {data.remove(0)});
		objectInstance.setActive(bitSet.get(7));
		objectInstance.setChatterFilter(bitSet.get(5));
		objectInstance.setLocalForced(bitSet.get(4));
		objectInstance.setRemoteForced(bitSet.get(3));
		objectInstance.setCommunicationsLost(bitSet.get(2));
		objectInstance.setRestart(bitSet.get(1));
		objectInstance.setOnline(bitSet.get(0));
		
		return objectInstance;
	}
}