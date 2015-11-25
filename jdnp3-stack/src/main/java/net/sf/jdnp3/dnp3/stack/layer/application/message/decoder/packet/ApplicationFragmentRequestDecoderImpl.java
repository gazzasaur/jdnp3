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

import static java.lang.String.format;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationControlField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;

public class ApplicationFragmentRequestDecoderImpl implements ApplicationFragmentRequestDecoder {
	private Logger logger = LoggerFactory.getLogger(ApplicationFragmentRequestDecoderImpl.class);
	
	private ObjectFragmentDecoder objectFragmentDecoder;
	private ApplicationFragmentRequestHeaderDecoder decoder = new ApplicationFragmentRequestHeaderDecoder();
	
	public ApplicationFragmentRequestDecoderImpl(ObjectFragmentDecoder objectFragmentDecoder) {
		this.objectFragmentDecoder = objectFragmentDecoder;
	}
	
	public ApplicationFragmentRequest decode(ApplicationFragmentDecoderContext decoderContext, ApplicationFragmentRequest applicationFragmentRequest, List<Byte> data) {
		decoder.decode(applicationFragmentRequest, data);
		decoderContext.setFunctionCode(applicationFragmentRequest.getHeader().getFunctionCode());
		ApplicationControlField applicationControl = applicationFragmentRequest.getHeader().getApplicationControl();
		decoderContext.addDecodeLogic(format("Control Flags\t: %s, %s, %s, %s", applicationControl.isFirstFragmentOfMessage() ? "First Fragment" : "Not First Fragment", applicationControl.isFinalFragmentOfMessage() ? "Final Fragment" : "Not Final Fragment", applicationControl.isConfirmationRequired() ? "Confirmation Required": "Confirmation Not Required", applicationControl.isUnsolicitedResponse() ? "Unsolicit Response" : "Not Unsolicit Response"));
		decoderContext.addDecodeLogic(format("Sequence Number\t: %s (%02X)", applicationControl.getSequenceNumber(), applicationControl.getSequenceNumber()));
		decoderContext.addDecodeLogic(format("Function Code\t: %s (%02X)", decoderContext.getFunctionCode(), decoderContext.getFunctionCode().getCode()));
		
		while (data.size() > 0) {
			ObjectFragment objectFragment = new ObjectFragment();
			objectFragmentDecoder.decode(decoderContext, objectFragment, data);
			applicationFragmentRequest.addObjectFragment(objectFragment);
		}
		
		logger.debug(decoderContext.getDecodeLogic());
		
		return applicationFragmentRequest;
	}
}
