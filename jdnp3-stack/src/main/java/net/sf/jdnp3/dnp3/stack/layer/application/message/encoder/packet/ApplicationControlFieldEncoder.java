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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import java.util.BitSet;
import java.util.Deque;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationControlField;

public class ApplicationControlFieldEncoder {
	public void encode(ApplicationControlField applicationControlField, Deque<Byte> data) {
		BitSet flags = new BitSet(8);
		flags.set(7, applicationControlField.isFirstFragmentOfMessage());
		flags.set(6, applicationControlField.isFinalFragmentOfMessage());
		flags.set(5, applicationControlField.isConfirmationRequired());
		flags.set(4, applicationControlField.isUnsolicitedResponse());
		
		byte[] flagData = flags.toByteArray();
		byte fieldData = (flagData.length > 0) ? flagData[0] : 0;
		fieldData |= applicationControlField.getSequenceNumber() & 0x0F;
		data.add(fieldData);
	}
}
