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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;

public class StandardItemEnumeratorFactory implements ItemEnumeratorFactory {
	public boolean hasEnumerator(ApplicationFragmentDecoderContext decoderContext, ObjectFragment objectFragment) {
		try {
			this.createEnumerator(decoderContext, objectFragment);
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public ItemEnumerator createEnumerator(ApplicationFragmentDecoderContext decoderContext, ObjectFragment objectFragment) {
		PrefixType prefixType = objectFragment.getObjectFragmentHeader().getPrefixType();
		Range range = objectFragment.getObjectFragmentHeader().getRange();
		
		if (prefixType instanceof NoPrefixType) {
			if (range instanceof CountRange) {
				return new NoPrefixCountRangeItemEnumerator((CountRange) range);
			} else if (range instanceof IndexRange) {
				return new NoPrefixIndexRangeItemEnumerator((IndexRange) range);
			} else if (range instanceof NoRange) {
				return new NoPrefixNoRangeItemEnumerator();
			}
		} else if (prefixType instanceof IndexPrefixType) {
			if (range instanceof CountRange) {
				return new IndexPrefixCountRangeItemEnumerator((IndexPrefixType) prefixType, (CountRange) range);
			}
		}
		throw new IllegalArgumentException(String.format("No enumerator exist for the prefix type %s and range type %s.", decoderContext.getClass(), objectFragment.getClass()));
	}
}
