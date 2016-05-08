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
package net.sf.jdnp3.dnp3.service.outstation.adaptor;

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.READ;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.WRITE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.TIME_AND_DATE_ABSOLUTE_TIME;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.time.TimeAndDateRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.TimeAndDateObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class TimeAndDateRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private TimeAndDateRequestHandler serviceRequestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if ((functionCode == READ || functionCode == WRITE) && request.getObjectFragmentHeader().getObjectType().getGroup() == TIME_AND_DATE_ABSOLUTE_TIME.getGroup()) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		if (serviceRequestHandler != null) {
			List<TimeAndDateObjectInstance> result = null;
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof CountRange) {
				CountRange countRange = (CountRange) range;
				if (functionCode == READ) {
					result = serviceRequestHandler.doReadTime(countRange.getCount());
				} else if (functionCode == WRITE) {
					result = new ArrayList<>();
					for (ObjectInstance objectInstance : request.getObjectInstances()) {
						serviceRequestHandler.doWriteTime((TimeAndDateObjectInstance) objectInstance);
					}
				}
			}
			
			if (result == null) {
				logger.warn(String.format("Cannot perform a %s request on a TimeAndDate object with the range type of %s", functionCode, range.getClass()));
			} else {
				for (TimeAndDateObjectInstance objectInstance : result) {
					objectInstance.setRequestedType(request.getObjectFragmentHeader().getObjectType());
					response.add(objectInstance);
				}
			}
		}
	}
	
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof TimeAndDateRequestHandler) {
			this.serviceRequestHandler = (TimeAndDateRequestHandler) requestHandler;
		}
	}
}
