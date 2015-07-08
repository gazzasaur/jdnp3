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

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;

public class ApplicationFragmentRequestDecoderImpl implements ApplicationFragmentRequestDecoder {
	private ApplicationFragmentRequestHeaderDecoder decoder = new ApplicationFragmentRequestHeaderDecoder();
	private ObjectFragmentDecoder objectFragmentDecoder = new ObjectFragmentDecoder();
	
	public ApplicationFragmentRequest decode(List<Byte> data) {
		ApplicationFragmentRequest applicationFragmentRequest = new ApplicationFragmentRequest();
		decoder.decode(applicationFragmentRequest, data);
		
		while (data.size() > 0) {
			ObjectFragment objectFragment = new ObjectFragment();
			objectFragmentDecoder.decode(applicationFragmentRequest.getHeader().getFunctionCode(), objectFragment, data);
			applicationFragmentRequest.addObjectFragment(objectFragment);
		}
		
		return applicationFragmentRequest;
	}
}
