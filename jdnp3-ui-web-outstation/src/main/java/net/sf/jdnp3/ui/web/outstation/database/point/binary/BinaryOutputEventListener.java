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
package net.sf.jdnp3.ui.web.outstation.database.point.binary;

import java.util.Date;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputEventObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.EventListener;

public class BinaryOutputEventListener implements EventListener {
	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryOutputEventListener.class);
	
	private Outstation outstation;

	public BinaryOutputEventListener(Outstation outstation) {
		this.outstation = outstation;
	}
	
	public void eventReceived(DataPoint dataPoint) {
		this.eventReceived(dataPoint, new Date().getTime());
	}

	public void eventReceived(DataPoint dataPoint, long timestamp) {
		if (dataPoint instanceof BinaryOutputDataPoint) {
			BinaryOutputEventObjectInstance binaryOutputEventObjectInstance = new BinaryOutputEventObjectInstance();
			try {
				BinaryOutputDataPoint binaryDataPoint = (BinaryOutputDataPoint) dataPoint;
				PropertyUtils.copyProperties(binaryOutputEventObjectInstance, binaryDataPoint);
				binaryOutputEventObjectInstance.setTimestamp(timestamp);
				binaryOutputEventObjectInstance.setEventClass(binaryDataPoint.getEventClass());
				binaryOutputEventObjectInstance.setRequestedType(binaryDataPoint.getEventType());
				outstation.sendEvent(binaryOutputEventObjectInstance);
			} catch (Exception e) {
				LOGGER.error("Failed to send event.", e);
			}
		}
	}
}
