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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet;

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.BinaryInputStaticObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class0ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class1ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class2ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class3ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.InternalIndicatorBitObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;

public class ObjectFragmentDataDecoder {
	@SuppressWarnings("serial")
	private List<ObjectTypeDecoder> objectTypeDecoders = new ArrayList<ObjectTypeDecoder>() {{
		this.add(new BinaryInputStaticObjectTypeDecoder());
		this.add(new Class0ObjectTypeDecoder());
		this.add(new Class1ObjectTypeDecoder());
		this.add(new Class2ObjectTypeDecoder());
		this.add(new Class3ObjectTypeDecoder());
		this.add(new InternalIndicatorBitObjectTypeDecoder());
	}};
	
	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		boolean decoded = false;
		for (ObjectTypeDecoder objectTypeDecoder : objectTypeDecoders) {
			if (objectTypeDecoder.canDecode(functionCode, objectFragment)) {
				objectTypeDecoder.decode(functionCode, objectFragment, data);
				decoded = true;
				break;
			}
		}
		if (!decoded) {
			throw new IllegalArgumentException(format("No decoder was found for %s %s.", functionCode, objectFragment.getObjectFragmentHeader().getObjectType()));
		}
	}
}
