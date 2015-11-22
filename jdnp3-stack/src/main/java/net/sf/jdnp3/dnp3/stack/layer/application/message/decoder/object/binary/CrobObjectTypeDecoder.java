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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.binary;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_COMMAND_CROB;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.getInteger;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputCrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.OperationType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.TripCloseCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StatusCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class CrobObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(ObjectFragmentDecoderContext decoderContext) {
		return decoderContext.getObjectType().equals(BINARY_OUTPUT_COMMAND_CROB);
	}
	
	public ObjectInstance decode(ObjectFragmentDecoderContext decoderContext, List<Byte> data) {
		if (!this.canDecode(decoderContext)) {
			throw new IllegalArgumentException("Unable to decode data.");
		}
		
		BinaryOutputCrobObjectInstance crob = new BinaryOutputCrobObjectInstance();
		crob.setIndex(decoderContext.getCurrentIndex());
		
		long firstByte = DataUtils.getInteger(0, 1, data);
		long lastByte = DataUtils.getInteger(10, 1, data);
		long opTypeCode = firstByte & 0xF;
		long tccCode = (firstByte >> 6) & 0x3;
		long statusCode = lastByte & 0x7F;
		crob.setClear((firstByte & (1 << 5)) != 0);
		crob.setQueue((firstByte & (1 << 4)) != 0);
		
		for (OperationType ot : OperationType.values()) {
			if (ot.getCode() == opTypeCode) {
				crob.setOperationType(ot);
				break;
			}
		}
		
		for (TripCloseCode tcc : TripCloseCode.values()) {
			if (tcc.getCode() == tccCode) {
				crob.setTripCloseCode(tcc);
				break;
			}
		}
		
		for (StatusCode status : StatusCode.values()) {
			if (status.getCode() == statusCode) {
				crob.setStatusCode(status);
				break;
			}
		}
		
		crob.setCount(getInteger(1, 1, data));
		crob.setOnTime(getInteger(2, 4, data));
		crob.setOffTime(getInteger(6, 4, data));
		
		DataUtils.trim(11, data);
		return crob;
	}
}
