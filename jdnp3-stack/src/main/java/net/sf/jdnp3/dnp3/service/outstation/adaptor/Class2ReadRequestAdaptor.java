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
package net.sf.jdnp3.dnp3.service.outstation.adaptor;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.ANY;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.Class2ReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Class2ReadRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Class2ReadRequestHandler serviceRequestHandler = null;
	
	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == FunctionCode.READ && request.getObjectFragmentHeader().getObjectType().equals(ObjectTypeConstants.CLASS_2)) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			List<ObjectInstance> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof NoRange) {
				result = serviceRequestHandler.doReadClass(outstationEventQueue);
			} else if (range instanceof CountRange) {
				CountRange countRange = (CountRange) range;
				result = serviceRequestHandler.doReadClass(outstationEventQueue, countRange.getCount());
			}
			
			if (result == null) {
				logger.warn("Cannot perform a read request on the class for the range type of: " + range.getClass());
			} else {
				for (ObjectInstance objectInstance : result) {
					if (objectInstance.getRequestedType().equals(ANY)) {
						objectInstance.setRequestedType(request.getObjectFragmentHeader().getObjectType());
					}
					response.add(objectInstance);
				}
			}
		}
	}
	
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof Class2ReadRequestHandler) {
			this.serviceRequestHandler = (Class2ReadRequestHandler) requestHandler;
		}
	}
}
