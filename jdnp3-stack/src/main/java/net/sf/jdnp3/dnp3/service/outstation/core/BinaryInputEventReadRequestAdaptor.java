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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.READ;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_ANY;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryInputEventReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryInputEventReadRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private BinaryInputEventReadRequestHandler serviceRequestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == READ && request.getObjectFragmentHeader().getObjectType().getGroup() == BINARY_INPUT_EVENT_ANY.getGroup()) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			List<BinaryInputEventObjectInstance> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof NoRange) {
				result = serviceRequestHandler.doReadEvents();
			} else if (range instanceof CountRange) {
				CountRange countRange = (CountRange) range;
				result = serviceRequestHandler.doReadEvents(countRange.getCount());
			}
			
			if (result == null) {
				logger.warn("Cannot perform a read request on the event binary input on the range type of: " + range.getClass());
			} else {
				for (BinaryInputEventObjectInstance binaryInputEventObjectInstance : result) {
					binaryInputEventObjectInstance.setRequestedType(request.getObjectFragmentHeader().getObjectType());
					response.add(binaryInputEventObjectInstance);
				}
			}
		}
	}
	
	public void setServiceRequestHandler(ServiceRequestHandler serviceRequestHandler) {
		if (serviceRequestHandler instanceof BinaryInputEventReadRequestHandler) {
			this.serviceRequestHandler = (BinaryInputEventReadRequestHandler) serviceRequestHandler;
		}
	}
}
