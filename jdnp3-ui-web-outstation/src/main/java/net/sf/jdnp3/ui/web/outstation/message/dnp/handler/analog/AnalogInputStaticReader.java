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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.AnalogInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;

import org.apache.commons.beanutils.BeanUtils;

public class AnalogInputStaticReader implements AnalogInputStaticReadRequestHandler {
	private DatabaseManager databaseManager;

	public AnalogInputStaticReader(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<AnalogInputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, dataPoints.get((int) i));
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<AnalogInputStaticObjectInstance> readStatics() {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		for (AnalogInputDataPoint analogDataPoint : dataPoints) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, analogDataPoint);
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<AnalogInputStaticObjectInstance> readStatic(long index) {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		if (index < dataPoints.size()) {
			AnalogInputStaticObjectInstance analogInputStaticObjectInstance = new AnalogInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(analogInputStaticObjectInstance, dataPoints.get((int) index));
				points.add(analogInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public Class<AnalogInputStaticObjectInstance> getObjectInstanceClass() {
		return AnalogInputStaticObjectInstance.class;
	}
}