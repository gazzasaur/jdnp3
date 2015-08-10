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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponse;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

public class ApplicationFragmentResponseEncoderImpl implements ApplicationFragmentResponseEncoder {
	private ApplicationFragmentResponseHeaderEncoder applicationHeaderEncoder = new ApplicationFragmentResponseHeaderEncoder();
	private ObjectFragmentEncoder objectFragmentEncoder = new ObjectFragmentEncoder();
	
	public List<Byte> encode(ApplicationFragmentResponse fragment) {
		List<Byte> data = new ArrayList<>();
		applicationHeaderEncoder.encode(fragment.getHeader(), data);
		
		ObjectType objectType = null;
		ObjectFragmentEncoderContext context = new ObjectFragmentEncoderContext();
		context.setFunctionCode(fragment.getHeader().getFunctionCode());
		
		List<ObjectInstance> likeInstances = new ArrayList<>();
		for (ObjectInstance objectInstance : fragment.getObjectInstances()) {
			if (objectType == null) {
				objectType = objectInstance.getRequestedType();
				likeInstances.add(objectInstance);
			} else if (!objectType.equals(objectInstance.getRequestedType())) {
				context.setObjectType(objectType);
				objectFragmentEncoder.encode(context, likeInstances, data);
				likeInstances.clear();
				objectType = objectInstance.getRequestedType();
				likeInstances.add(objectInstance);
			} else {
				likeInstances.add(objectInstance);
			}
		}
		if (objectType != null) {
			context.setObjectType(objectType);
			objectFragmentEncoder.encode(context, likeInstances, data);
		}
		return data;
	}
}
