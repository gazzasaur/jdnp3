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
package net.sf.jdnp3.dnp3.service.outstation.core;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.RequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryInputStaticReadRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BinaryInputStaticReadRequestHandler serviceRequestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == FunctionCode.READ && request.getObjectFragmentHeader().getObjectType().getGroup() == ObjectTypeConstants.BINARY_INPUT_STATIC_ANY.getGroup()) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			List<BinaryInputStaticObjectInstance> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof NoRange) {
				result = serviceRequestHandler.doReadStatics();
			} else if (range instanceof IndexRange) {
				IndexRange indexRange = (IndexRange) range;
				result = serviceRequestHandler.doReadStatics(indexRange.getStartIndex(), indexRange.getStopIndex());
			}
			
			if (result == null) {
				logger.warn("Cannot perform a read request on the static binary input on the range type of: " + range.getClass());
			} else {
				for (BinaryInputStaticObjectInstance binaryInputStaticObjectInstance : result) {
					binaryInputStaticObjectInstance.setRequestedType(request.getObjectFragmentHeader().getObjectType());
					response.add(binaryInputStaticObjectInstance);
				}
			}
		}
	}
	
	public void setRequestHandler(RequestHandler requestHandler) {
		if (requestHandler instanceof BinaryInputStaticReadRequestHandler) {
			this.serviceRequestHandler = (BinaryInputStaticReadRequestHandler) requestHandler;
		}
	}
}
