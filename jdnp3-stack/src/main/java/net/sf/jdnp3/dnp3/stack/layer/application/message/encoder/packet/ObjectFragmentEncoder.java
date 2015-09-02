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

import static java.lang.String.format;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectTypeEncoderConstants.OBJECT_TYPE_ENCODERS;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectFragmentHeaderEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class ObjectFragmentEncoder {
	private ObjectFragmentHeaderEncoder objectFragmentHeaderEncoder = new ObjectFragmentHeaderEncoder();
	
	public void encode(ObjectFragmentEncoderContext context, ObjectFragment objectFragment, List<Byte> data) {
		objectFragmentHeaderEncoder.encode(objectFragment.getObjectFragmentHeader(), data);
		PrefixType prefixType = objectFragment.getObjectFragmentHeader().getPrefixType();
		
		if (objectFragment.getObjectInstances().size() > 0) {
			context.setStartIndex(objectFragment.getObjectInstances().get(0).getIndex());
		}
		
		ObjectTypeEncoder objectTypeEncoder = null;
		for (ObjectTypeEncoder encoder : OBJECT_TYPE_ENCODERS) {
			if (encoder.canEncode(context.getFunctionCode(), context.getObjectType())) {
				objectTypeEncoder = encoder;
			}
		}
		if (objectTypeEncoder == null) {
			throw new IllegalArgumentException(format("Failed to encode %s %s", context.getFunctionCode(), context.getObjectType()));
		}
		
		for (ObjectInstance objectInstance : objectFragment.getObjectInstances()) {
			context.setCurrentIndex(objectInstance.getIndex());
			PrefixTypeEncoder.encode(prefixType, objectInstance, data);
			objectTypeEncoder.encode(context, objectInstance, data);
		}
	}
}
