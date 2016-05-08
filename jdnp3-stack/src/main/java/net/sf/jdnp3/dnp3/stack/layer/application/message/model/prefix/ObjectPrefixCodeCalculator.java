/**
 * Copyright 2016 Graeme Farquharson
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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix;

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.FOUR_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.FOUR_OCTET_LENGTH;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.ONE_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.ONE_OCTET_LENGTH;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.TWO_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode.TWO_OCTET_LENGTH;

import java.util.Arrays;
import java.util.List;

public class ObjectPrefixCodeCalculator {
	private static final List<ObjectPrefixCode> LENGTHS = Arrays.asList(ONE_OCTET_LENGTH, TWO_OCTET_LENGTH, FOUR_OCTET_LENGTH);
	private static final List<ObjectPrefixCode> INDICIES = Arrays.asList(ONE_OCTET_INDEX, TWO_OCTET_INDEX, FOUR_OCTET_INDEX);

	public static ObjectPrefixCode calculate(PrefixType prefixType) {
		return ObjectPrefixCodeCalculator.calculate(prefixType, prefixType.getOctetCount());
	}

	public static ObjectPrefixCode calculate(PrefixType prefixType, int minOctetCount) {
		if (prefixType instanceof IndexPrefixType) {
			for (ObjectPrefixCode objectPrefixCode : INDICIES) {
				if (objectPrefixCode.getOctetCount() == minOctetCount) {
					return objectPrefixCode;
				}
			}
			throw new IllegalArgumentException("Cannot encode a prefix of length: " + prefixType.getOctetCount());
		} else if (prefixType instanceof LengthPrefixType) {
			for (ObjectPrefixCode objectPrefixCode : LENGTHS) {
				if (objectPrefixCode.getOctetCount() == minOctetCount) {
					return objectPrefixCode;
				}
			}
			throw new IllegalArgumentException("Cannot encode a prefix of length: " + prefixType.getOctetCount());
		}
		return ObjectPrefixCode.NONE;
	}
	
	public static ObjectPrefixCode calculateIndexPrefix(long value) {
		return calculatePrefix(value, INDICIES);
	}
	
	public static ObjectPrefixCode calculateLengthPrefix(long value) {
		return calculatePrefix(value, LENGTHS);
	}
	
	private static ObjectPrefixCode calculatePrefix(long value, List<ObjectPrefixCode> options) {
		if (value >= 0) {
			for (ObjectPrefixCode objectPrefixCode : options) {
				if (value < (1 << 8*objectPrefixCode.getOctetCount())) {
					return objectPrefixCode;
				}
			}
		}
		throw new IllegalArgumentException("Cannot fit the value " + value + " in an object prefix code.");
	}
}