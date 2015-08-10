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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.AnalogInputStaticFloat16ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.AnalogInputStaticFloat64ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.BinaryInputEventAbsoluteTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.BinaryInputEventWithoutTimeTimeObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.BinaryInputStaticFlagsObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.BinaryInputStaticPackedObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class ObjectFragmentEncoder {
	@SuppressWarnings("serial")
	private List<ObjectTypeEncoder> objectTypeEncoders = new ArrayList<ObjectTypeEncoder>() {{
		this.add(new BinaryInputStaticPackedObjectTypeEncoder());
		this.add(new BinaryInputStaticFlagsObjectTypeEncoder());
		
		this.add(new BinaryInputEventWithoutTimeTimeObjectTypeEncoder());
		this.add(new BinaryInputEventAbsoluteTimeObjectTypeEncoder());
		
		this.add(new AnalogInputStaticFloat16ObjectTypeEncoder());
		this.add(new AnalogInputStaticFloat64ObjectTypeEncoder());
	}};
	
	public void encode(ObjectFragmentEncoderContext context, List<ObjectInstance> objectInstances, List<Byte> data) {
		boolean encoded = false;
		
		for (ObjectTypeEncoder objectTypeEncoder : objectTypeEncoders) {
			if (objectTypeEncoder.canEncode(context.getFunctionCode(), context.getObjectType())) {
				objectTypeEncoder.encode(context, objectInstances, data);
				encoded = true;
				break;
			}
		}
		if (!encoded) {
			throw new IllegalArgumentException(format("Failed to encode %s %s", context.getFunctionCode(), context.getObjectType()));
		}
	}
}
