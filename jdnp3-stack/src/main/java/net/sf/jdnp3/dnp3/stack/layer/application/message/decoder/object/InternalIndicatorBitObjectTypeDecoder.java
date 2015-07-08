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

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.InternalIndicatorBitObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class InternalIndicatorBitObjectTypeDecoder implements ObjectTypeDecoder {
	public void decode(long startIndex, long stopIndex, ObjectField objectField, List<Byte> data) {
		long value = DataUtils.getInteger(0, 1, data);
		long byteIndex = objectField.getPrefix() % 8;
		BitSet valueFlags = BitSet.valueOf(new long[] { value });
		InternalIndicatorBitObjectInstance internalIndicatorBit = new InternalIndicatorBitObjectInstance();
		internalIndicatorBit.setActive(valueFlags.get((int) byteIndex));
		internalIndicatorBit.setIndex(objectField.getPrefix());
		objectField.setObjectInstance(internalIndicatorBit);
		
		if (objectField.getPrefix() == stopIndex) {
			DataUtils.trim(1, data);
		}
	}
}
