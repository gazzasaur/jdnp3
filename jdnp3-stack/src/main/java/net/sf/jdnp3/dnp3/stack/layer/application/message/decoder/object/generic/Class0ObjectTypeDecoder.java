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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants;

public class Class0ObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ApplicationFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(ObjectTypeConstants.CLASS_0);
	}
	
	public ObjectInstance decode(ApplicationFragmentDecoderContext decoderContext, List<Byte> data) {
		throw new UnsupportedOperationException("Object does not contain data.");
	}
}
