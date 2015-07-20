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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.INTERNAL_INDICATIONS_PACKED;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.InternalIndicatorBitObjectInstance;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class InternalIndicatorBitObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(FunctionCode functionCode, ObjectFragment objectFragment) {
		return functionCode.equals(FunctionCode.WRITE)
				&& objectFragment.getObjectFragmentHeader().getObjectType().equals(INTERNAL_INDICATIONS_PACKED)
				&& objectFragment.getObjectFragmentHeader().getPrefixType() instanceof NoPrefixType
				&& objectFragment.getObjectFragmentHeader().getRange() instanceof IndexRange;
	}

	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		if (!this.canDecode(functionCode, objectFragment)) {
			throw new IllegalArgumentException("Cannot decode data.");
		}
		IndexRange indexRange = (IndexRange) objectFragment.getObjectFragmentHeader().getRange();
		
		int dataSize = 1;
		if (indexRange.getStartIndex() < 8 && indexRange.getStopIndex() > 7) {
			dataSize = 2;
		}
		long value = DataUtils.getInteger(0, dataSize, data);
		DataUtils.trim(dataSize, data);
		
		for (long i = indexRange.getStartIndex(); i <= indexRange.getStopIndex(); ++i) {
			InternalIndicatorBitObjectInstance objectInstance = new InternalIndicatorBitObjectInstance();
			objectInstance.setIndex(i);
			objectInstance.setActive((value & (1 << i)) != 0);
			objectFragment.addObjectInstance(objectInstance);
		}
	}
}
