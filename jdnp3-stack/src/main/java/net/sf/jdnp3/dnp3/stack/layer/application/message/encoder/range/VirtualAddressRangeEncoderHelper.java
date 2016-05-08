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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.FOUR_OCTET_VIRTUAL_ADDRESS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.ONE_OCTET_VIRTUAL_ADDRESS;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode.TWO_OCTET_VIRTUAL_ADDRESS;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VirtualAddressRange;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class VirtualAddressRangeEncoderHelper implements RangeEncoderHelper {
	private List<RangeSpecifierCode> codes = Arrays.asList(ONE_OCTET_VIRTUAL_ADDRESS, TWO_OCTET_VIRTUAL_ADDRESS, FOUR_OCTET_VIRTUAL_ADDRESS);
	
	public RangeSpecifierCode calculateRangeSpecifierCode(Range range, int minOctetCount) {
		VirtualAddressRange specificRange = (VirtualAddressRange) range;
		for (RangeSpecifierCode rangeSpecifierCode : codes) {
			if (specificRange.getStopAddress() < rangeSpecifierCode.getUpperLimit() && rangeSpecifierCode.getOctetCount() >= minOctetCount) {
				return rangeSpecifierCode;
			}
		}
		throw new IllegalArgumentException("The specified address is too large for DNP: " + specificRange.getStopAddress());
	}
	
	public RangeSpecifierCode encode(Range range, int minOctetCount, List<Byte> data) {
		VirtualAddressRange specificRange = (VirtualAddressRange) range;
		RangeSpecifierCode rangeSpecifierCode = this.calculateRangeSpecifierCode(specificRange, minOctetCount);
		DataUtils.addInteger(specificRange.getStartAddress(), rangeSpecifierCode.getOctetCount(), data);
		DataUtils.addInteger(specificRange.getStopAddress(), rangeSpecifierCode.getOctetCount(), data);
		return rangeSpecifierCode;
	}
}
