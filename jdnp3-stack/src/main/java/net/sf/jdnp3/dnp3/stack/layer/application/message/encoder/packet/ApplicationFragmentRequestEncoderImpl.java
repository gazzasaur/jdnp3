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

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;

public class ApplicationFragmentRequestEncoderImpl implements ApplicationFragmentRequestEncoder {
	private ObjectFragmentEncoder objectFragmentEncoder;
	private ApplicationFragmentRequestHeaderEncoder applicationHeaderEncoder = new ApplicationFragmentRequestHeaderEncoder();
	
	public ApplicationFragmentRequestEncoderImpl(ObjectFragmentEncoder objectFragmentEncoder) {
		this.objectFragmentEncoder = objectFragmentEncoder;
	}
	
	public List<Byte> encode(ApplicationFragmentRequest fragment) {
		List<Byte> data = new ArrayList<>();
		applicationHeaderEncoder.encode(fragment.getHeader(), data);
		
		ObjectFragmentEncoderContext context = new ObjectFragmentEncoderContext();
		context.setFunctionCode(fragment.getHeader().getFunctionCode());
		context.setCommonTimeOfOccurrance(0);
		for (ObjectFragment objectFragment : fragment.getObjectFragments()) {
			context.setObjectType(objectFragment.getObjectFragmentHeader().getObjectType());
			objectFragmentEncoder.encode(context, objectFragment, data);
		}
		return data;
	}
}
