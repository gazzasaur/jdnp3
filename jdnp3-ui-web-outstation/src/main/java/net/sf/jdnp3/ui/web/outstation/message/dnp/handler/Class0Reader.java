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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.Class0ReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;

import org.apache.commons.beanutils.BeanUtils;

public class Class0Reader implements Class0ReadRequestHandler {
	public List<ObjectInstance> doReadClass() {
		List<ObjectInstance> points = new ArrayList<>();
		
		List<BinaryInputDataPoint> binaryDataPoints = DatabaseManagerProvider.getDatabaseManager().getBinaryDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoint);
				binaryInputStaticObjectInstance.setRequestedType(binaryDataPoint.getStaticType());
				points.add(binaryInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		List<AnalogInputDataPoint> analogDataPoints = DatabaseManagerProvider.getDatabaseManager().getAnalogDataPoints();
		for (AnalogInputDataPoint analogDataPoint : analogDataPoints) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, analogDataPoint);
				analogInputStaticObjectInstance.setRequestedType(analogDataPoint.getStaticType());
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return points;
	}
}
