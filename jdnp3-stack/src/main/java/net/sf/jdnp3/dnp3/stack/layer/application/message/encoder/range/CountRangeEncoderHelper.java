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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_OBJECT_COUNT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_OBJECT_COUNT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_OBJECT_COUNT;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class CountRangeEncoderHelper implements RangeEncoderHelper {
	private List<RangeSpecifierCode> codes = Arrays.asList(ONE_OCTET_OBJECT_COUNT, TWO_OCTET_OBJECT_COUNT, FOUR_OCTET_OBJECT_COUNT);
	
	public RangeSpecifierCode calculateRangeSpecifierCode(Range range) {
		CountRange specificRange = (CountRange) range;
		for (RangeSpecifierCode rangeSpecifierCode : codes) {
			if (specificRange.getCount() < rangeSpecifierCode.getUpperLimit()) {
				return rangeSpecifierCode;
			}
		}
		throw new IllegalArgumentException("The specified count is too large for DNP: " + specificRange.getCount());
	}
	
	public RangeSpecifierCode encode(Range range, List<Byte> data) {
		CountRange specificRange = (CountRange) range;
		RangeSpecifierCode rangeSpecifierCode = this.calculateRangeSpecifierCode(specificRange);
		DataUtils.addInteger(specificRange.getCount(), rangeSpecifierCode.getOctetCount(), data);
		return rangeSpecifierCode;
	}
}
