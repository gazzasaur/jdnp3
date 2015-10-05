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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CLASS_2;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class Class2ObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(CLASS_2);
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		throw new UnsupportedOperationException("Object does not contain data.");
	}
}
