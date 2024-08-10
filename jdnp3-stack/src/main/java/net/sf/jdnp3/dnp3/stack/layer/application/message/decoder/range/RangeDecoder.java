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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.range;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragmentHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;

public class RangeDecoder {
	private Map<RangeSpecifierCode, RangeDecoderHelper> helpers = new HashMap<RangeSpecifierCode, RangeDecoderHelper>() {{
		this.put(RangeSpecifierCode.NO_RANGE, new NoRangeDecoderHelper());
		this.put(RangeSpecifierCode.ONE_OCTET_INDEX, new IndexRangeDecoderHelper());
		this.put(RangeSpecifierCode.TWO_OCTET_INDEX, new IndexRangeDecoderHelper());
		this.put(RangeSpecifierCode.FOUR_OCTET_INDEX, new IndexRangeDecoderHelper());
		this.put(RangeSpecifierCode.ONE_OCTET_OBJECT_COUNT, new CountRangeDecoderHelper());
		this.put(RangeSpecifierCode.TWO_OCTET_OBJECT_COUNT, new CountRangeDecoderHelper());
		this.put(RangeSpecifierCode.FOUR_OCTET_OBJECT_COUNT, new CountRangeDecoderHelper());
		this.put(RangeSpecifierCode.ONE_OCTET_VIRTUAL_ADDRESS, new VirtualAddressRangeDecoderHelper());
		this.put(RangeSpecifierCode.TWO_OCTET_VIRTUAL_ADDRESS, new VirtualAddressRangeDecoderHelper());
		this.put(RangeSpecifierCode.FOUR_OCTET_VIRTUAL_ADDRESS, new VirtualAddressRangeDecoderHelper());
		this.put(RangeSpecifierCode.VARIABLE_FORMAT_QUALIFIER, new VariableFormatQualifierRangeDecoderHelper());
	}};
	
	public void decode(ObjectFragmentHeader header, List<Byte> data) {
		RangeDecoderHelper helper = helpers.get(header.getQualifierField().getRangeSpecifierCode());
		if (helper == null) {
			throw new IllegalStateException("No range decoder was found for the given range: " + header.getQualifierField().getRangeSpecifierCode());
		}
		header.setRange(helper.decode(header.getQualifierField().getRangeSpecifierCode(), data));
	}
}
