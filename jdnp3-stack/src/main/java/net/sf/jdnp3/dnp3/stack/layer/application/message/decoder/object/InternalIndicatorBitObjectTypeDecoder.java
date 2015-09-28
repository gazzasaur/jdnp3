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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.INTERNAL_INDICATIONS_PACKED;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.InternalIndicatorBitObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class InternalIndicatorBitObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(INTERNAL_INDICATIONS_PACKED);
	}

	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		if (!this.canDecode(decoderContext)) {
			throw new IllegalArgumentException("Cannot decode data.");
		}
		
		long value = DataUtils.getInteger(0, 1, data);
		if (decoderContext.getCurrentIndex() > 7) {
			value = DataUtils.getInteger(0, 1, data);
		}
		
		InternalIndicatorBitObjectInstance objectInstance = new InternalIndicatorBitObjectInstance();
		objectInstance.setIndex(decoderContext.getCurrentIndex());
		objectInstance.setActive((value & (1 << decoderContext.getCurrentIndex())) != 0);
		
		if (decoderContext.isLastItem()) {
			DataUtils.trim(1, data);
			if (decoderContext.getStartIndex() < 8 && decoderContext.getCurrentIndex() > 7) {
				DataUtils.trim(1, data);
			}
		}
		return objectInstance;
	}
}
