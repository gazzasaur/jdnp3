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
package net.sf.jdnp3.dnp3.stack.layer.application.service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;

public class OutstationEventQueue implements ConfirmationListener {
	private Logger logger = LoggerFactory.getLogger(OutstationEventQueue.class);
	
	private InternalStatusProvider internalStatusProvider = null;
	private List<EventObjectInstance> events = new LinkedList<>();
	private List<EventObjectInstance> pendingConfirmation = new LinkedList<>();
	
	public synchronized void setInternalStatusProvider(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public synchronized void addEvent(EventObjectInstance eventObjectInstance) {
		if (eventObjectInstance.getEventClass() < 1 || eventObjectInstance.getEventClass() > 3) {
			logger.info("Ignoring event of type {} and event class of {}.", eventObjectInstance.getClass(), eventObjectInstance.getEventClass());
			return;
		}
		
		ListIterator<EventObjectInstance> currentEvents = events.listIterator();
		while (currentEvents.hasNext()) {
			EventObjectInstance current = currentEvents.next();
			if (eventObjectInstance.getTimestamp() < current.getTimestamp()) {
				currentEvents.previous();
				currentEvents.add(eventObjectInstance);
				setInternalStatus();
				return;
			}			
		}
		events.add(eventObjectInstance);
		setInternalStatus();
	}

	public synchronized List<ObjectInstance> request(EventObjectInstanceSelector selector) {
		List<ObjectInstance> requestedEvents = new ArrayList<>();
		for (EventObjectInstance eventObjectInstance : events) {
			if (selector.select(eventObjectInstance) && !pendingConfirmation.contains(eventObjectInstance)) {
				requestedEvents.add(eventObjectInstance);
				pendingConfirmation.add(eventObjectInstance);
			}
		}
		setInternalStatus();
		return requestedEvents;
	}

	public synchronized List<ObjectInstance> request(EventObjectInstanceSelector selector, long returnLimit) {
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
		setInternalStatus();
		return requestedEvents;
	}

	public synchronized void confirm(EventObjectInstance eventObjectInstance) {
		events.remove(eventObjectInstance);
		pendingConfirmation.remove(eventObjectInstance);
		setInternalStatus();
	}

	public synchronized void timedOut(EventObjectInstance eventObjectInstance) {
		pendingConfirmation.remove(eventObjectInstance);
		setInternalStatus();
	}

	public synchronized void cancelled(EventObjectInstance eventObjectInstance) {
		pendingConfirmation.remove(eventObjectInstance);
		setInternalStatus();
	}
	
	private void setInternalStatus() {
		if (internalStatusProvider == null) {
			return;
		}
		boolean[] classEvents = {false, false, false};
		for (EventObjectInstance eventObjectInstance : events) {
			if (eventObjectInstance.getEventClass() >= 1 && eventObjectInstance.getEventClass() <= 3 && !pendingConfirmation.contains(eventObjectInstance)) {
				classEvents[eventObjectInstance.getEventClass() - 1] = true;
			}
		}
		internalStatusProvider.setClass1Events(classEvents[0]);
		internalStatusProvider.setClass2Events(classEvents[1]);
		internalStatusProvider.setClass3Events(classEvents[2]);
	}
}
