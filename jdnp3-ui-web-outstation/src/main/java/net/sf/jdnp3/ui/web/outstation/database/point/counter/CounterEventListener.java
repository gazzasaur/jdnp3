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
package net.sf.jdnp3.ui.web.outstation.database.point.counter;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterEventObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.EventListener;

public class CounterEventListener implements EventListener {
	private Logger logger = LoggerFactory.getLogger(CounterEventListener.class);
	
	private Outstation outstation;

	public CounterEventListener(Outstation outstation) {
		this.outstation = outstation;
	}
	
	public void eventReceived(DataPoint dataPoint) {
		if (dataPoint instanceof CounterDataPoint) {
			CounterEventObjectInstance counterEventObjectInstance = new CounterEventObjectInstance();
			try {
				CounterDataPoint counterDataPoint = (CounterDataPoint) dataPoint;
				BeanUtils.copyProperties(counterEventObjectInstance, counterDataPoint);
				counterEventObjectInstance.setTimestamp(new Date().getTime());
				counterEventObjectInstance.setEventClass(counterDataPoint.getEventClass());
				counterEventObjectInstance.setRequestedType(counterDataPoint.getEventType());
				outstation.sendEvent(counterEventObjectInstance);
			} catch (Exception e) {
				logger.error("Failed to send event.", e);
			}
		}
	}
}
