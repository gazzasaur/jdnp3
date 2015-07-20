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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.VARIABLE_FORMAT_QUALIFIER;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VariableFormatQualifierRange;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class VariableFormatQualifierRangeEncoderHelper implements RangeEncoderHelper {
	
	public RangeSpecifierCode calculateRangeSpecifierCode(Range range, int minOctetCount) {
		VariableFormatQualifierRange specificRange = (VariableFormatQualifierRange) range;
		if (specificRange.getCount() < VARIABLE_FORMAT_QUALIFIER.getUpperLimit() && VARIABLE_FORMAT_QUALIFIER.getOctetCount() >= minOctetCount) {
			return VARIABLE_FORMAT_QUALIFIER;
		}
		throw new IllegalArgumentException("The specified size is too large for DNP: " + specificRange.getCount());
	}
	
	public RangeSpecifierCode encode(Range range, int minOctetCount, List<Byte> data) {
		VariableFormatQualifierRange specificRange = (VariableFormatQualifierRange) range;
		RangeSpecifierCode rangeSpecifierCode = this.calculateRangeSpecifierCode(specificRange, minOctetCount);
		DataUtils.addInteger(specificRange.getCount(), rangeSpecifierCode.getOctetCount(), data);
		return rangeSpecifierCode;
	}
}
