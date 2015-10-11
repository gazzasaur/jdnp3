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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.exception.UnknownObjectException;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.ItemEnumerator;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.ItemEnumeratorFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCodeUtils;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.NullObjectInstance;

public class ObjectFragmentDataDecoder {
	private List<ObjectTypeDecoder> objectTypeDecoders = new ArrayList<>();
	private List<ItemEnumeratorFactory> itemEnumeratorFactories = new ArrayList<>();
	
	public void addObjectTypeDecoder(ObjectTypeDecoder objectTypeDecoder) {
		objectTypeDecoders.add(objectTypeDecoder);
	}

	public void addItemEnumeratorFactory(ItemEnumeratorFactory itemEnumeratorFactory) {
		itemEnumeratorFactories.add(itemEnumeratorFactory);
	}

	public void decode(ObjectFragmentDecoderContext decoderContext, ObjectFragment objectFragment, List<Byte> data) {
		Range range = objectFragment.getObjectFragmentHeader().getRange();
		PrefixType prefixType = objectFragment.getObjectFragmentHeader().getPrefixType();
		
		ItemEnumerator itemEnumerator = null;
		for (ItemEnumeratorFactory itemEnumeratorFactory : itemEnumeratorFactories) {
			if (itemEnumeratorFactory.hasEnumerator(decoderContext, objectFragment)) {
				itemEnumerator = itemEnumeratorFactory.createEnumerator(decoderContext, objectFragment);
				break;
			}
		}
		if (itemEnumerator == null) {
			throw new IllegalArgumentException(String.format("Cannot enumerate over ObjectType %s, PrefixType %s and Range %s.", objectFragment.getObjectFragmentHeader().getObjectType(), prefixType, range));
		}
		
		ObjectTypeDecoder objectTypeDecoder = null;
		for (ObjectTypeDecoder decoder : objectTypeDecoders) {
			if (decoder.canDecode(decoderContext)) {
				objectTypeDecoder = decoder;
			}
		}
		if (objectTypeDecoder == null) {
			throw new UnknownObjectException("Unknown object: " + decoderContext.getObjectType());
		}
		
		boolean firstPass = true;
		while (itemEnumerator.hasNext()) {
			long index = itemEnumerator.next(data);
			if (firstPass) {
				firstPass = false;
				decoderContext.setStartIndex(index);
			}
			decoderContext.setCurrentIndex(index);
			decoderContext.setLastItem(!itemEnumerator.hasNext());
					
			if (FunctionCodeUtils.hasObjectInstanceData(decoderContext.getFunctionCode())) {
				objectFragment.addObjectInstance(objectTypeDecoder.decode(decoderContext, data));
			} else if (objectFragment.getObjectFragmentHeader().getPrefixType() instanceof IndexPrefixType) {
				NullObjectInstance nullObjectInstance = new NullObjectInstance();
				nullObjectInstance.setIndex(index);
				nullObjectInstance.setRequestedType(objectFragment.getObjectFragmentHeader().getObjectType());
				objectFragment.addObjectInstance(nullObjectInstance);
			}
		}
	}
}
