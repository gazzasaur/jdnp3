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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.counter;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.handler.counter.CounterStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class CounterStaticHandler implements CounterStaticReadRequestHandler {
	private Logger logger = LoggerFactory.getLogger(CounterStaticHandler.class);
	
	private DatabaseManager databaseManager;

	public CounterStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<CounterStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<CounterStaticObjectInstance> points = new ArrayList<>();
		List<CounterDataPoint> dataPoints = databaseManager.getCounterDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			CounterDataPoint dataPoint = dataPoints.get((int) i);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public List<CounterStaticObjectInstance> readStatics() {
		List<CounterStaticObjectInstance> points = new ArrayList<>();
		List<CounterDataPoint> dataPoints = databaseManager.getCounterDataPoints();

		for (CounterDataPoint dataPoint : dataPoints) {
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public List<CounterStaticObjectInstance> readStatic(long index) {
		List<CounterStaticObjectInstance> points = new ArrayList<>();
		List<CounterDataPoint> dataPoints = databaseManager.getCounterDataPoints();

		if (index < dataPoints.size()) {
			CounterDataPoint dataPoint = dataPoints.get((int) index);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public Class<CounterStaticObjectInstance> getObjectInstanceClass() {
		return CounterStaticObjectInstance.class;
	}
	
	private void copyDataPoint(List<CounterStaticObjectInstance> points, CounterDataPoint dataPoint) {
		CounterStaticObjectInstance objectInstance = new CounterStaticObjectInstance();
		try {
			BeanUtils.copyProperties(objectInstance, dataPoint);
			objectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(objectInstance);
		} catch (Exception e) {
			logger.error("Cannot copy data point.", e);
		}
	}
}