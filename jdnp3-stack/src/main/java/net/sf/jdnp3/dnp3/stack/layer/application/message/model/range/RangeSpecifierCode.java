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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.range;

import java.math.BigInteger;

public enum RangeSpecifierCode {
	ONE_OCTET_INDEX(0x00, 1),
	TWO_OCTET_INDEX(0x01, 2),
	FOUR_OCTET_INDEX(0x02, 4),
	ONE_OCTET_VIRTUAL_ADDRESS(0x03, 1),
	TWO_OCTET_VIRTUAL_ADDRESS(0x04, 2),
	FOUR_OCTET_VIRTUAL_ADDRESS(0x05, 4),
	NO_RANGE(0x06, 0),
	ONE_OCTET_OBJECT_COUNT(0x07, 1),
	TWO_OCTET_OBJECT_COUNT(0x08, 2),
	FOUR_OCTET_OBJECT_COUNT(0x09, 4),
	VARIABLE_FORMAT_QUALIFIER(0x0B, 2);
	
	private final int code;
	private final int octetCount;
	
	RangeSpecifierCode(int code, int octetCount) {
		this.code = code;
		this.octetCount = octetCount;
	}

	public int getCode() {
		return code;
	}

	public int getOctetCount() {
		return octetCount;
	}

	public long getUpperLimit() {
		return BigInteger.valueOf(2L).pow(8*octetCount).longValue();
	}
}
