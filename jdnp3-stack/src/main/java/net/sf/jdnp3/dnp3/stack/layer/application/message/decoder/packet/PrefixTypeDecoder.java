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

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragmentHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.LengthPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.ObjectPrefixCode;

public class PrefixTypeDecoder {
	public void decode(ObjectFragmentHeader objectFragmentHeader) {
		ObjectPrefixCode objectPrefixCode = objectFragmentHeader.getQualifierField().getObjectPrefixCode();
		if (objectPrefixCode.equals(ObjectPrefixCode.ONE_OCTET_INDEX) || objectPrefixCode.equals(ObjectPrefixCode.TWO_OCTET_INDEX) || objectPrefixCode.equals(ObjectPrefixCode.FOUR_OCTET_INDEX)) {
			IndexPrefixType indexPrefixType = new IndexPrefixType();
			indexPrefixType.setOctetCount(objectPrefixCode.getOctetCount());
			objectFragmentHeader.setPrefixType(indexPrefixType);
		} else if (objectPrefixCode.equals(ObjectPrefixCode.ONE_OCTET_LENGTH) || objectPrefixCode.equals(ObjectPrefixCode.TWO_OCTET_LENGTH) || objectPrefixCode.equals(ObjectPrefixCode.FOUR_OCTET_LENGTH)) {
			LengthPrefixType lengthPrefixType = new LengthPrefixType();
			lengthPrefixType.setOctetCount(objectPrefixCode.getOctetCount());
			objectFragmentHeader.setPrefixType(lengthPrefixType);
		} else {
			NoPrefixType noPrefixType = new NoPrefixType();
			objectFragmentHeader.setPrefixType(noPrefixType);
		}
	}
}
