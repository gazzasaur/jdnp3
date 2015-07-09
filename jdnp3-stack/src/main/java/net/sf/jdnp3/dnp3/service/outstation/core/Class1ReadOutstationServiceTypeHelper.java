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

import net.sf.jdnp3.dnp3.service.outstation.handler.Class1ReadRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.RequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Class1ReadOutstationServiceTypeHelper implements OutstationServiceTypeHelper {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Class1ReadRequestHandler requestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		System.out.println(request.getObjectFragmentHeader().getObjectType());
		System.out.println(ObjectTypeConstants.CLASS_1);
		System.out.println(functionCode == FunctionCode.READ);
		
		if (functionCode == FunctionCode.READ && request.getObjectFragmentHeader().getObjectType().equals(ObjectTypeConstants.CLASS_1)) {
			System.out.println("YES");
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, ObjectFragment request, List<ObjectInstance> response) {
		if (requestHandler != null) {
			List<ObjectInstance> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof NoRange) {
				result = requestHandler.doReadClass(null);
			} else if (range instanceof CountRange) {
				CountRange countRange = (CountRange) range;
				result = requestHandler.doReadClass(null, countRange.getCount());
			}
			
			if (result == null) {
				logger.warn("Cannot perform a read request on the class for the range type of: " + range.getClass());
			} else {
				for (ObjectInstance objectInstance : result) {
					response.add(objectInstance);
				}
			}
		}
	}
	
	public boolean setHandler(RequestHandler requestHandler) {
		if (requestHandler instanceof Class1ReadRequestHandler) {
			this.requestHandler = (Class1ReadRequestHandler) requestHandler;
			return true;
		}
		return false;
	}
}
