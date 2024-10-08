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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class IndexRangeDecoderHelper implements RangeDecoderHelper {
	public Range decode(RangeSpecifierCode rangeSpecifierCode, List<Byte> data) {
		IndexRange range = new IndexRange();
		range.setStartIndex(DataUtils.getUnsignedInteger(0, rangeSpecifierCode.getOctetCount(), data));
		range.setStopIndex(DataUtils.getUnsignedInteger(rangeSpecifierCode.getOctetCount(), rangeSpecifierCode.getOctetCount(), data));
		DataUtils.trim(2*rangeSpecifierCode.getOctetCount(), data);
		return range;
	}
}
