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
package net.sf.jdnp3.dnp3.stack.layer.application;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.main.EventObjectInstanceSelector;

public class OutstationEventQueue implements ConfirmationListener {
	private List<EventObjectInstance> events = new ArrayList<>();
	private List<EventObjectInstance> pendingConfirmation = new ArrayList<>();
	
	public synchronized void addEvent(EventObjectInstance eventObjectInstance) {
		for (int i = 0; i < events.size(); ++i) {
			EventObjectInstance current = events.get(i);
			if (eventObjectInstance.getTimestamp() < current.getTimestamp()) {
				events.add(i, eventObjectInstance);
				return;
			}
		}
		events.add(eventObjectInstance);
	}

	public synchronized List<ObjectInstance> request(EventObjectInstanceSelector selector) {
		List<ObjectInstance> requestedEvents = new ArrayList<>();
		for (EventObjectInstance eventObjectInstance : events) {
			if (selector.select(eventObjectInstance) && !pendingConfirmation.contains(eventObjectInstance)) {
				requestedEvents.add(eventObjectInstance);
				pendingConfirmation.add(eventObjectInstance);
			}
		}
		return requestedEvents;
	}

	public List<ObjectInstance> request(EventObjectInstanceSelector selector, long returnLimit) {
		List<ObjectInstance> requestedEvents = new ArrayList<>();
		for (EventObjectInstance eventObjectInstance : events) {
			if (selector.select(eventObjectInstance) && !pendingConfirmation.contains(eventObjectInstance)) {
				requestedEvents.add(eventObjectInstance);
				pendingConfirmation.add(eventObjectInstance);
				if (requestedEvents.size() == returnLimit) {
					break;
				}
			}
		}
		return requestedEvents;
	}

	public synchronized void confirm(EventObjectInstance eventObjectInstance) {
		events.remove(eventObjectInstance);
		pendingConfirmation.remove(eventObjectInstance);
	}

	public synchronized void timedOut(EventObjectInstance eventObjectInstance) {
		pendingConfirmation.remove(eventObjectInstance);
	}

	public synchronized void cancelled(EventObjectInstance eventObjectInstance) {
		pendingConfirmation.remove(eventObjectInstance);
	}
}
