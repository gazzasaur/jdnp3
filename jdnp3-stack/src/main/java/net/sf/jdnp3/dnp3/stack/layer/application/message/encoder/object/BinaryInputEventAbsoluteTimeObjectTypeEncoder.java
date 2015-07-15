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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCodeCalculator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class BinaryInputEventAbsoluteTimeObjectTypeEncoder implements ObjectTypeEncoder {
	public ObjectPrefixCode calculateObjectPrefix(long maxPrefix) {
		return ObjectPrefixCodeCalculator.calculateIndexPrefix(maxPrefix);
	}
	
	public Range calculateRangeType(long count, long startPrefix, long stopPrefix, ObjectInstance lastObjectInstance) {
		CountRange range = new CountRange();
		range.setCount(count);
		return range;
	}

	public boolean fragment(ObjectInstance currentObjectInstance, ObjectInstance previousObjectInstance) {
		return false;
	}
	
	public void encode(long startPrefix, ObjectField objectField, List<Byte> data) {
		BinaryInputEventObjectInstance binary = (BinaryInputEventObjectInstance) objectField.getObjectInstance();
		BitSet bitSet = new BitSet();
		bitSet.set(7, binary.isActive());
		bitSet.set(5, binary.isChatterFilter());
		bitSet.set(4, binary.isLocalForced());
		bitSet.set(3, binary.isRemoteForced());
		bitSet.set(2, binary.isCommunicationsLost());
		bitSet.set(1, binary.isRestart());
		bitSet.set(0, binary.isOnline());
		
		byte[] rawValue = bitSet.toByteArray();
		byte value = 0;
		if (rawValue.length > 0) {
			value = rawValue[0];
		}
		data.add(value);

		DataUtils.addInteger(binary.getTimestamp(), 6, data);
	}
}
