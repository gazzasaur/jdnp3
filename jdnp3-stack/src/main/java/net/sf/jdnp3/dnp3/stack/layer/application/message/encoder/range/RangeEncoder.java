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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VariableFormatQualifierRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VirtualAddressRange;

public class RangeEncoder {
	@SuppressWarnings("serial")
	private Map<Class<?>, RangeEncoderHelper> helpers = new HashMap<Class<?>, RangeEncoderHelper>() {{
		this.put(IndexRange.class, new IndexRangeEncoderHelper());
		this.put(VirtualAddressRange.class, new VirtualAddressRangeEncoderHelper());
		this.put(NoRange.class, new NoRangeEncoderHelper());
		this.put(CountRange.class, new CountRangeEncoderHelper());
		this.put(VariableFormatQualifierRange.class, new VariableFormatQualifierRangeEncoderHelper());
	}};
	
	public RangeSpecifierCode calculateRangeSpecifierCode(Range range) {
		RangeEncoderHelper helper = helpers.get(range.getClass());
		if (helper == null) {
			throw new RuntimeException("No RangeEncoderHelper found for class " + range.getClass());
		}
		return helper.calculateRangeSpecifierCode(range);
	}
	
	// FIXME IMPL It may not long be necessary to return the RSC for cross validation as this is now pre-calculated.
	public RangeSpecifierCode encode(Range range, List<Byte> data) {
		RangeEncoderHelper helper = helpers.get(range.getClass());
		if (helper == null) {
			throw new RuntimeException("No RangeEncoderHelper found for class " + range.getClass());
		}
		return helper.encode(range, data);
	}
}
