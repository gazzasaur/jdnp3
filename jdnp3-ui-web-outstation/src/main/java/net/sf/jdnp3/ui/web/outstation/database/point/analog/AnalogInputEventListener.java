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
package net.sf.jdnp3.ui.web.outstation.database.point.analog;

import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputEventObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.EventListener;

public class AnalogInputEventListener implements EventListener {
	private Logger logger = LoggerFactory.getLogger(AnalogInputEventListener.class);
	
	private Outstation outstation;

	public AnalogInputEventListener(Outstation outstation) {
		this.outstation = outstation;
	}
	
	public void eventReceived(DataPoint dataPoint) {
		if (dataPoint instanceof AnalogInputDataPoint) {
			AnalogInputEventObjectInstance analogInputEventObjectInstance = new AnalogInputEventObjectInstance();
			try {
				AnalogInputDataPoint analogDataPoint = (AnalogInputDataPoint) dataPoint;
				BeanUtils.copyProperties(analogInputEventObjectInstance, analogDataPoint);
				analogInputEventObjectInstance.setTimestamp(new Date().getTime());
				analogInputEventObjectInstance.setEventClass(analogDataPoint.getEventClass());
				analogInputEventObjectInstance.setRequestedType(analogDataPoint.getEventType());
				outstation.sendEvent(analogInputEventObjectInstance);
			} catch (Exception e) {
				logger.error("Failed to send event.", e);
			}
		}
	}
}
