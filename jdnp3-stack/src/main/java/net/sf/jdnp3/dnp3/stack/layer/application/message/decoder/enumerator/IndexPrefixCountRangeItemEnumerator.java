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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator;

import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.getUnsignedInteger;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.trim;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;

public class IndexPrefixCountRangeItemEnumerator implements ItemEnumerator {
	private long count = 0;
	private CountRange countRange;
	private IndexPrefixType indexPrefixType;
	
	public IndexPrefixCountRangeItemEnumerator(IndexPrefixType indexPrefixType, CountRange countRange) {
		this.countRange = countRange;
		this.indexPrefixType = indexPrefixType;
	}
	
	public boolean hasNext() {
		return count < countRange.getCount();
	}
	
	public long next(List<Byte> data) {
		if (!this.hasNext()) {
			throw new IllegalStateException("No items remain.");
		}
		++count;
		long index = getUnsignedInteger(0, indexPrefixType.getOctetCount(), data);
		trim(indexPrefixType.getOctetCount(), data);
		return index;
	}
}
