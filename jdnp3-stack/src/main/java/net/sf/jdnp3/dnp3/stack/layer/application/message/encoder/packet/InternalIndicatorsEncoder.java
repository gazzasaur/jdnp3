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
package net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet;

import java.util.BitSet;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.InternalIndicatorField;

public class InternalIndicatorsEncoder {
	
	public void encode(InternalIndicatorField internalIndicatorField, List<Byte> data) {
		BitSet flags = new BitSet(16);
		flags.set(13, internalIndicatorField.isConfigurationCorrupt());
		flags.set(12, internalIndicatorField.isAlreadyExecuting());
		flags.set(11, internalIndicatorField.isEventBufferOverflow());
		flags.set(10, internalIndicatorField.isParameterError());
		flags.set(9, internalIndicatorField.isObjectUnknown());
		flags.set(8, internalIndicatorField.isNoFunctionCodeSupport());
		flags.set(7, internalIndicatorField.isDeviceRestart());
		flags.set(6, internalIndicatorField.isDeviceTrouble());
		flags.set(5, internalIndicatorField.isLocalControl());
		flags.set(4, internalIndicatorField.isNeedTime());
		flags.set(3, internalIndicatorField.isClass3Events());
		flags.set(2, internalIndicatorField.isClass2Events());
		flags.set(1, internalIndicatorField.isClass1Events());
		flags.set(0, internalIndicatorField.isBroadcast());
		
		byte[] flagData = flags.toByteArray();
		byte fieldData0 = (flagData.length > 0) ? flagData[0] : 0;
		byte fieldData1 = (flagData.length > 1) ? flagData[1] : 0;
		data.add(fieldData0);
		data.add(fieldData1);
	}
}
