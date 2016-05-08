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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.range;

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_INDEX;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_INDEX;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class IndexRangeEncoderHelper implements RangeEncoderHelper {
	private List<RangeSpecifierCode> codes = Arrays.asList(ONE_OCTET_INDEX, TWO_OCTET_INDEX, FOUR_OCTET_INDEX);
	
	public RangeSpecifierCode calculateRangeSpecifierCode(Range range, int minOctetCount) {
		IndexRange specificRange = (IndexRange) range;
		for (RangeSpecifierCode rangeSpecifierCode : codes) {
			if (specificRange.getStopIndex() < rangeSpecifierCode.getUpperLimit() && rangeSpecifierCode.getOctetCount() >= minOctetCount) {
				return rangeSpecifierCode;
			}
		}
		throw new IllegalArgumentException("The specified index is too large for DNP: " + specificRange.getStopIndex());
	}
	
	public RangeSpecifierCode encode(Range range, int minOctetCount, List<Byte> data) {
		IndexRange specificRange = (IndexRange) range;
		RangeSpecifierCode rangeSpecifierCode = calculateRangeSpecifierCode(specificRange, minOctetCount);
		DataUtils.addInteger(specificRange.getStartIndex(), rangeSpecifierCode.getOctetCount(), data);
		DataUtils.addInteger(specificRange.getStopIndex(), rangeSpecifierCode.getOctetCount(), data);
		return rangeSpecifierCode;
	}
}
