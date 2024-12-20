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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.WRITE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.INTERNAL_INDICATIONS_PACKED;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.OutstationRequestHandlerAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.InternalIndicatorWriteRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.InternalIndicatorBitObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class InternalIndicatorWriteRequestAdaptor implements OutstationRequestHandlerAdaptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(InternalIndicatorWriteRequestAdaptor.class);
	
	private InternalIndicatorWriteRequestHandler serviceRequestHandler = null;

	public boolean canHandle(FunctionCode functionCode, ObjectFragment request) {
		if (functionCode == WRITE && request.getObjectFragmentHeader().getObjectType().equals(INTERNAL_INDICATIONS_PACKED)) {
			return true;
		}
		return false;
	}
	
	public void doRequest(FunctionCode functionCode, OutstationEventQueue outstationEventQueue, ObjectFragment request, List<ObjectInstance> response) {
		// FIXME IMPL Need to check 'canHandle' again in all of these.
		if (serviceRequestHandler != null) {
			Range range = request.getObjectFragmentHeader().getRange();
			
			if (range instanceof IndexRange) {
				IndexRange indexRange = (IndexRange) range;
				long index = indexRange.getStartIndex();
				for (ObjectInstance objectInstance : request.getObjectInstances()) {
					InternalIndicatorBitObjectInstance specificObjectInstance = (InternalIndicatorBitObjectInstance) objectInstance;
					serviceRequestHandler.doWriteIndicatorBit(index, specificObjectInstance.isActive());
				}
			} else {
				LOGGER.warn("Cannot perform a %s request on the type %s for the range type of %s and prefix type %s.", WRITE, INTERNAL_INDICATIONS_PACKED, range, request.getObjectFragmentHeader().getPrefixType());
			}
		}
	}
	
	public void setRequestHandler(OutstationRequestHandler requestHandler) {
		if (requestHandler instanceof InternalIndicatorWriteRequestHandler) {
			this.serviceRequestHandler = (InternalIndicatorWriteRequestHandler) requestHandler;
		}
	}
}
