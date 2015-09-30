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

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.TIME_AND_DATE_ABSOLUTE_TIME;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.getInteger;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.trim;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.TimeAndDateObjectInstance;

public class TimeAndDateObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(TIME_AND_DATE_ABSOLUTE_TIME);
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		if (!this.canDecode(decoderContext)) {
			throw new IllegalArgumentException("Unable to decode data.");
		}
		
		TimeAndDateObjectInstance objectInstance = new TimeAndDateObjectInstance();
		objectInstance.setIndex(decoderContext.getCurrentIndex());
		objectInstance.setRequestedType(decoderContext.getObjectType());
		objectInstance.setTimestamp(getInteger(0, 6, data));
		trim(6, data);
		
		return objectInstance;
	}
}
