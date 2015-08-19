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
package net.sf.jdnp3.dnp3.stack.layer.application.service;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequestHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;

public class ApplicationLayerUtils {
	public static ApplicationFragmentRequest createMasterRequest(FunctionCode functionCode, int sequenceNumber) {
		ApplicationFragmentRequest request = new ApplicationFragmentRequest();
		ApplicationFragmentRequestHeader header = request.getHeader();
		header.getApplicationControl().setConfirmationRequired(false);
		header.getApplicationControl().setFirstFragmentOfMessage(true);
		header.getApplicationControl().setFinalFragmentOfMessage(true);
		header.getApplicationControl().setSequenceNumber(sequenceNumber);
		header.getApplicationControl().setUnsolicitedResponse(false);
		header.setFunctionCode(functionCode);
		return request;
	}
}