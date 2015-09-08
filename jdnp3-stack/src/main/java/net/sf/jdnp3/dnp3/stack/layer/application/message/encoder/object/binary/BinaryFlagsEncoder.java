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

import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.bitSetToByte;

import java.util.BitSet;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryOutputStaticObjectInstance;

public class BinaryFlagsEncoder {
	public static byte encode(BinaryInputStaticObjectInstance objectInstance) {
		BitSet bitSet = new BitSet(8);
		bitSet.set(7, objectInstance.isActive());
		bitSet.set(5, objectInstance.isChatterFilter());
		bitSet.set(4, objectInstance.isLocalForced());
		bitSet.set(3, objectInstance.isRemoteForced());
		bitSet.set(2, objectInstance.isCommunicationsLost());
		bitSet.set(1, objectInstance.isRestart());
		bitSet.set(0, objectInstance.isOnline());
		
		return bitSetToByte(bitSet);
	}
	
	public static byte encode(BinaryInputEventObjectInstance objectInstance) {
		BitSet bitSet = new BitSet(8);
		bitSet.set(7, objectInstance.isActive());
		bitSet.set(5, objectInstance.isChatterFilter());
		bitSet.set(4, objectInstance.isLocalForced());
		bitSet.set(3, objectInstance.isRemoteForced());
		bitSet.set(2, objectInstance.isCommunicationsLost());
		bitSet.set(1, objectInstance.isRestart());
		bitSet.set(0, objectInstance.isOnline());
		
		return bitSetToByte(bitSet);
	}
	
	public static byte encode(BinaryOutputStaticObjectInstance objectInstance) {
		BitSet bitSet = new BitSet(8);
		bitSet.set(7, objectInstance.isActive());
		bitSet.set(4, objectInstance.isLocalForced());
		bitSet.set(3, objectInstance.isRemoteForced());
		bitSet.set(2, objectInstance.isCommunicationsLost());
		bitSet.set(1, objectInstance.isRestart());
		bitSet.set(0, objectInstance.isOnline());
		
		return bitSetToByte(bitSet);
	}
}