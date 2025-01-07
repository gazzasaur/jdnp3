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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.handler.analog.AnalogOutputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;

public class AnalogOutputStaticHandler implements AnalogOutputStaticReadRequestHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(AnalogOutputStaticHandler.class);
	
	private DatabaseManager databaseManager;

	public AnalogOutputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<AnalogOutputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<AnalogOutputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogOutputDataPoint> dataPoints = databaseManager.getAnalogOutputDataPoints();

		for (AnalogOutputDataPoint dataPoint : dataPoints) {
			if (dataPoint.getIndex() >= startIndex && dataPoint.getIndex() <= stopIndex) {
				copyDataPoint(points, dataPoint);
			}
		}
		return points;
	}

	public List<AnalogOutputStaticObjectInstance> readStatics() {
		List<AnalogOutputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogOutputDataPoint> dataPoints = databaseManager.getAnalogOutputDataPoints();

		for (AnalogOutputDataPoint analogDataPoint : dataPoints) {
			copyDataPoint(points, analogDataPoint);
		}
		return points;
	}

	public List<AnalogOutputStaticObjectInstance> readStatic(long index) {
		List<AnalogOutputStaticObjectInstance> points = new ArrayList<>();
		List<AnalogOutputDataPoint> dataPoints = databaseManager.getAnalogOutputDataPoints();
		dataPoints.stream().filter(dataPoint -> dataPoint.getIndex() == index).findAny().ifPresent(dataPoint -> copyDataPoint(points, dataPoint));
		return points;
	}

	public Class<AnalogOutputStaticObjectInstance> getObjectInstanceClass() {
		return AnalogOutputStaticObjectInstance.class;
	}

	private void copyDataPoint(List<AnalogOutputStaticObjectInstance> points, AnalogOutputDataPoint dataPoint) {
		AnalogOutputStaticObjectInstance objectInstance = new AnalogOutputStaticObjectInstance();
		try {
			PropertyUtils.copyProperties(objectInstance, dataPoint);
			objectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(objectInstance);
		} catch (Exception e) {
			LOGGER.error("Cannot copy data point.", e);
		}
	}
}