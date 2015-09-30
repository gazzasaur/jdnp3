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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.QualifierField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;

public class QualifierDecoder {
	public void decode(QualifierField qualifierField, List<Byte> data) {
		for (Byte dataByte : data) {
			System.out.print(String.format("%02X", dataByte));
		}
		System.out.println();
		
		byte value = data.remove(0);
		int prefixCodeValue = (value >> 4) & 0x07;
		int rangeSpecifierCodeValue = value & 0x0F;
		
		boolean found = false;
		for (ObjectPrefixCode objectPrefixCode : ObjectPrefixCode.values()) {
			if (objectPrefixCode.getCode() == prefixCodeValue) {
				qualifierField.setObjectPrefixCode(objectPrefixCode);
				found = true;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("Unknwn object prefix code: " + prefixCodeValue);
		}
		
		found = false;
		for (RangeSpecifierCode rangeSpecifierCode : RangeSpecifierCode.values()) {
			if (rangeSpecifierCode.getCode() == rangeSpecifierCodeValue) {
				qualifierField.setRangeSpecifierCode(rangeSpecifierCode);
				found = true;
			}
		}
		if (!found) {
			throw new IllegalArgumentException("Unknwn range specifier code: " + rangeSpecifierCodeValue);
		}
	}
}
