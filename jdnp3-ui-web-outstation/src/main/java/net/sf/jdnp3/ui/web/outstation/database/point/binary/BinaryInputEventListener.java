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
package net.sf.jdnp3.ui.web.outstation.database.point.binary;

import java.util.Date;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputEventObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.EventListener;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BinaryInputEventListener implements EventListener {
	private Logger logger = LoggerFactory.getLogger(BinaryInputEventListener.class);
	
	private Outstation outstation;

	public BinaryInputEventListener(Outstation outstation) {
		this.outstation = outstation;
	}
	
	public void eventReceived(DataPoint dataPoint) {
		if (dataPoint instanceof BinaryInputDataPoint) {
			BinaryInputEventObjectInstance binaryInputEventObjectInstance = new BinaryInputEventObjectInstance();
			try {
				BinaryInputDataPoint binaryDataPoint = (BinaryInputDataPoint) dataPoint;
				BeanUtils.copyProperties(binaryInputEventObjectInstance, binaryDataPoint);
				binaryInputEventObjectInstance.setTimestamp(new Date().getTime());
				binaryInputEventObjectInstance.setEventClass(binaryDataPoint.getEventClass());
				binaryInputEventObjectInstance.setRequestedType(binaryDataPoint.getEventType());
				outstation.sendEvent(binaryInputEventObjectInstance);
			} catch (Exception e) {
				logger.error("Failed to send event.", e);
			}
		}
	}
}
