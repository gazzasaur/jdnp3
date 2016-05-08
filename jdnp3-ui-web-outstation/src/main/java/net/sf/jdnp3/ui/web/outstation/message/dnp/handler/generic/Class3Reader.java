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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic;

import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.generic.Class3ReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.EventObjectInstanceSelector;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationEventQueue;

public class Class3Reader implements Class3ReadRequestHandler {
	public List<ObjectInstance> doReadClass(OutstationEventQueue outstationEventQueue) {
		EventObjectInstanceSelector selector = new EventObjectInstanceSelector() {
			public boolean select(EventObjectInstance eventObjectInstance) {
				return eventObjectInstance.getEventClass() == 3;
			}
		};
		return outstationEventQueue.request(selector);
	}

	public List<ObjectInstance> doReadClass(OutstationEventQueue outstationEventQueue, long returnLimit) {
		EventObjectInstanceSelector selector = new EventObjectInstanceSelector() {
			public boolean select(EventObjectInstance eventObjectInstance) {
				return eventObjectInstance.getEventClass() == 3;
			}
		};
		return outstationEventQueue.request(selector, returnLimit);
	}

}
