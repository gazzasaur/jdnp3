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

import net.sf.jdnp3.dnp3.service.outstation.handler.analog.AnalogInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AnalogInputStaticHandler implements AnalogInputStaticReadRequestHandler {
	private Logger logger = LoggerFactory.getLogger(AnalogInputStaticHandler.class);
	
	private DatabaseManager databaseManager;

	public AnalogInputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<AnalogInputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			AnalogInputDataPoint dataPoint = dataPoints.get((int) i);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public List<AnalogInputStaticObjectInstance> readStatics() {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		for (AnalogInputDataPoint analogDataPoint : dataPoints) {
			copyDataPoint(points, analogDataPoint);
		}
		return points;
	}

	public List<AnalogInputStaticObjectInstance> readStatic(long index) {
		List<AnalogInputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogInputDataPoint> dataPoints = databaseManager.getAnalogInputDataPoints();

		if (index < dataPoints.size()) {
			AnalogInputDataPoint dataPoint = dataPoints.get((int) index);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public Class<AnalogInputStaticObjectInstance> getObjectInstanceClass() {
		return AnalogInputStaticObjectInstance.class;
	}

	private void copyDataPoint(List<AnalogInputStaticObjectInstance> points, AnalogInputDataPoint dataPoint) {
		AnalogInputStaticObjectInstance objectInstance = new AnalogInputStaticObjectInstance();
		try {
			BeanUtils.copyProperties(objectInstance, dataPoint);
			objectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(objectInstance);
		} catch (Exception e) {
			logger.error("Cannot copy data point.", e);
		}
	}
}