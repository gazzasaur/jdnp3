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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range;

import static java.util.Arrays.asList;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util.EncoderUtils.calculateOctetCount;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_OBJECT_COUNT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_VIRTUAL_ADDRESS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.NO_RANGE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_OBJECT_COUNT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_VIRTUAL_ADDRESS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_OBJECT_COUNT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_VIRTUAL_ADDRESS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.VARIABLE_FORMAT_QUALIFIER;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VariableFormatQualifierRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VirtualAddressRange;

public class RangeSpecifierCodeCalculator {
	private static final List<RangeSpecifierCode> COUNTS = asList(ONE_OCTET_OBJECT_COUNT, TWO_OCTET_OBJECT_COUNT, FOUR_OCTET_OBJECT_COUNT);
	private static final List<RangeSpecifierCode> INDICIES = asList(ONE_OCTET_INDEX, TWO_OCTET_INDEX, FOUR_OCTET_INDEX);
	private static final List<RangeSpecifierCode> VIRTUAL_ADDRESSES = asList(ONE_OCTET_VIRTUAL_ADDRESS, TWO_OCTET_VIRTUAL_ADDRESS, FOUR_OCTET_VIRTUAL_ADDRESS);
	
	public static RangeSpecifierCode calculateRangeSpecifierCode(Range range, int minOctetCount) {
		if (range instanceof IndexRange) {
			IndexRange indexRange = (IndexRange) range;
			int octetCount = Math.max(calculateOctetCount(indexRange.getStopIndex()), minOctetCount);
			return calculateRangeSpecifierCode(INDICIES, octetCount);
		} else if (range instanceof VirtualAddressRange) {
			VirtualAddressRange virtualAddressRange = (VirtualAddressRange) range;
			int octetCount = Math.max(calculateOctetCount(virtualAddressRange.getStartAddress()), minOctetCount);
			return calculateRangeSpecifierCode(VIRTUAL_ADDRESSES, octetCount);
		} else if (range instanceof CountRange) {
			CountRange countRange = (CountRange) range;
			int octetCount = Math.max(calculateOctetCount(countRange.getCount()), minOctetCount);
			return calculateRangeSpecifierCode(COUNTS, octetCount);
		} else if (range instanceof VariableFormatQualifierRange) {
			return VARIABLE_FORMAT_QUALIFIER;
		} else {
			return NO_RANGE;
		}
	}
	
	private static RangeSpecifierCode calculateRangeSpecifierCode(List<RangeSpecifierCode> codes, int minOctetCount) {
		for (RangeSpecifierCode rangeSpecifierCode : codes) {
			if (rangeSpecifierCode.getOctetCount() >= minOctetCount) {
				return rangeSpecifierCode;
			}
		}
		throw new IllegalArgumentException("Unable to determine a range specifier code.");
	}
}
