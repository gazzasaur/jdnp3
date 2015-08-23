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

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.CROB;
import static net.sf.jdnp3.dnp3.stack.utils.DataUtils.getInteger;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.CrobObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.OperationType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.StatusCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.TripCloseCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class CrobObjectTypeDecoder implements ObjectTypeDecoder {
	public boolean canDecode(FunctionCode functionCode, ObjectFragment objectFragment) {
		return (functionCode.equals(FunctionCode.DIRECT_OPERATE))
				&& objectFragment.getObjectFragmentHeader().getObjectType().equals(CROB)
				&& objectFragment.getObjectFragmentHeader().getPrefixType() instanceof IndexPrefixType
				&& objectFragment.getObjectFragmentHeader().getRange() instanceof CountRange;
	}
	
	public void decode(FunctionCode functionCode, ObjectFragment objectFragment, List<Byte> data) {
		if (!this.canDecode(functionCode, objectFragment)) {
			throw new IllegalArgumentException("Unable to decode data.");
		}
		CountRange countRange = (CountRange)objectFragment.getObjectFragmentHeader().getRange();
		for (int i = 0; i < countRange.getCount(); ++i) {
			parseInstance(objectFragment, data);
		}
	}

	private void parseInstance(ObjectFragment objectFragment, List<Byte> data) {
		int prefixOctetCount = objectFragment.getObjectFragmentHeader().getPrefixType().getOctetCount();
		long index = DataUtils.getInteger(0, prefixOctetCount, data);
		DataUtils.trim(prefixOctetCount, data);
		
		CrobObjectInstance crob = new CrobObjectInstance();
		crob.setIndex(index);
		
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
		
		objectFragment.addObjectInstance(crob);
		DataUtils.trim(objectFragment.getObjectFragmentHeader().getPrefixType().getOctetCount() + 11, data);
	}
}
