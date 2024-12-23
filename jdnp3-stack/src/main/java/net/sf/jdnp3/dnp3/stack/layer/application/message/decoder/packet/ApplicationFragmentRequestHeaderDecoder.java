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
package net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequestHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class ApplicationFragmentRequestHeaderDecoder {
	public ApplicationControlFieldDecoder acfDecoder = new ApplicationControlFieldDecoder();
	
	public void decode(ApplicationFragmentRequest request, List<Byte> data) {
		if (data.size() < 2) {
			throw new IllegalStateException("Cannot decode request.  Data is too small.");
		}
		
		ApplicationFragmentRequestHeader header = request.getHeader();
		acfDecoder.decode(header.getApplicationControl(), data);
		
		boolean found = false;
		int funcionCodeValue = (int) DataUtils.getUnsignedInteger(0, 1, data);
		for (FunctionCode functionCode : FunctionCode.values()) {
			if (functionCode.getCode() == funcionCodeValue) {
				header.setFunctionCode(functionCode);
				found = true;
			}
		}
		if (!found) {
			throw new IllegalStateException(String.format("Unknown function code: %02X", funcionCodeValue));
		}
		if (!header.getFunctionCode().isRequestCode()) {
			throw new IllegalArgumentException("A response function code was received when a request was expected.");
		}
		
		data.remove(0);
	}
}
