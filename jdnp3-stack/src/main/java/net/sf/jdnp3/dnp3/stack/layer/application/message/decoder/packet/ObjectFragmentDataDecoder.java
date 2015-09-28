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

import static java.util.Arrays.asList;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE_NR;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.SELECT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.WRITE;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.ItemEnumerator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.ItemEnumeratorList;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.NullObjectInstance;

public class ObjectFragmentDataDecoder {
	private static final List<FunctionCode> CONTAINS_DATA = asList(WRITE, SELECT, OPERATE, DIRECT_OPERATE, DIRECT_OPERATE_NR);
	
	private List<ObjectTypeDecoder> objectTypeDecoders = new ArrayList<>();
	
	public void addObjectTypeDecoder(ObjectTypeDecoder objectTypeDecoder) {
		objectTypeDecoders.add(objectTypeDecoder);
	}
	
	public void decode(ObjectFragmentDecoderContext decoderContext, ObjectFragment objectFragment, List<Byte> data) {
		Range range = objectFragment.getObjectFragmentHeader().getRange();
		PrefixType prefixType = objectFragment.getObjectFragmentHeader().getPrefixType();
		ItemEnumerator itemEnumerator = ItemEnumeratorList.getEnumerator(prefixType, range);
		
		for (ObjectTypeDecoder objectTypeDecoder : objectTypeDecoders) {
			if (objectTypeDecoder.canDecode(decoderContext)) {
				boolean firstPass = true;
				
				while (itemEnumerator.hasNext()) {
					long index = itemEnumerator.next(data);
					if (firstPass) {
						firstPass = false;
						decoderContext.setStartIndex(index);
					}
					decoderContext.setCurrentIndex(index);
					decoderContext.setLastItem(!itemEnumerator.hasNext());
					
					if (CONTAINS_DATA.contains(decoderContext.getFunctionCode())) {
						objectFragment.addObjectInstance(objectTypeDecoder.decode(decoderContext, data));
					} else {
						NullObjectInstance nullObjectInstance = new NullObjectInstance();
						nullObjectInstance.setIndex(index);
						nullObjectInstance.setRequestedType(objectFragment.getObjectFragmentHeader().getObjectType());
						objectFragment.addObjectInstance(nullObjectInstance);
					}
				}
			}
		}
	}
}
