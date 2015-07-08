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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationControlField;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class ApplicationControlFieldDecoder {
	public void decode(ApplicationControlField applicationControlField, List<Byte> data) {
		int sequenceNumber = (int) DataUtils.getInteger8(0, data) & 0x0F;
		applicationControlField.setSequenceNumber(sequenceNumber);
		
		BitSet flags = BitSet.valueOf(new byte[] { data.remove(0) });
		applicationControlField.setFirstFragmentOfMessage(flags.get(7));
		applicationControlField.setFinalFragmentOfMessage(flags.get(6));
		applicationControlField.setConfirmationRequired(flags.get(5));
		applicationControlField.setUnsolicitedResponse(flags.get(4));
	}
}
