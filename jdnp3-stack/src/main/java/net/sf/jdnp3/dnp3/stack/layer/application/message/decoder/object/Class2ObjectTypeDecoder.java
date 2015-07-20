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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DISABLE_UNSOLICITED;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.ENABLE_UNSOLICITED;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.READ;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.Class2ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;

public class Class2ObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(FunctionCode functionCode, ObjectFragment objectFragment) {
		return (functionCode.equals(READ) || functionCode.equals(ENABLE_UNSOLICITED) || functionCode.equals(DISABLE_UNSOLICITED))
				&& objectFragment.getObjectFragmentHeader().getObjectType().equals(ObjectTypeConstants.CLASS_2)
				&& objectFragment.getObjectFragmentHeader().getPrefixType() instanceof NoPrefixType
				&& (objectFragment.getObjectFragmentHeader().getRange() instanceof NoRange
						|| objectFragment.getObjectFragmentHeader().getRange() instanceof CountRange);
	}
	
	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		if (!this.canDecode(functionCode, objectFragment)) {
			throw new IllegalArgumentException("Unable to decode data.");
		}
		objectFragment.addObjectInstance(new Class2ObjectInstance());
	}
}
