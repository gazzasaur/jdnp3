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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryOutputStaticAssignClassRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryOutputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;

import org.apache.commons.beanutils.BeanUtils;

public class BinaryOutputStaticHandler implements BinaryOutputStaticReadRequestHandler, BinaryOutputStaticAssignClassRequestHandler {
	private DatabaseManager databaseManager;

	public BinaryOutputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<BinaryOutputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			BinaryOutputDataPoint dataPoint = dataPoints.get((int) i);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}

	public List<BinaryOutputStaticObjectInstance> readStatics() {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (BinaryOutputDataPoint dataPoint : dataPoints) {
			copyDataPoint(points, dataPoint);
		}
		return points;
	}
	
	public List<BinaryOutputStaticObjectInstance> readStatic(long index) {
		List<BinaryOutputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		if (index < dataPoints.size()) {
			BinaryOutputDataPoint dataPoint = dataPoints.get((int) index);
			copyDataPoint(points, dataPoint);
		}
		return points;
	}
	
	public void assignClass(long index, long eventClass) {
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		if (index < dataPoints.size()) {
			BinaryOutputDataPoint dataPoint = dataPoints.get((int) index);
			dataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryOutputDataPoint(dataPoint);
		}
	}
	
	public void assignClasses(long eventClass) {
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (BinaryOutputDataPoint dataPoint : dataPoints) {
			dataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryOutputDataPoint(dataPoint);
		}
	}
	
	public void assignClasses(long startIndex, long stopIndex, long eventClass) {
		List<BinaryOutputDataPoint> dataPoints = databaseManager.getBinaryOutputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			BinaryOutputDataPoint dataPoint = dataPoints.get((int) i);
			dataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryOutputDataPoint(dataPoint);
		}
	}

	public Class<BinaryOutputStaticObjectInstance> getObjectInstanceClass() {
		return BinaryOutputStaticObjectInstance.class;
	}

	private void copyDataPoint(List<BinaryOutputStaticObjectInstance> points, BinaryOutputDataPoint dataPoint) {
		BinaryOutputStaticObjectInstance staticObjectInstance = new BinaryOutputStaticObjectInstance();
		try {
			BeanUtils.copyProperties(staticObjectInstance, dataPoint);
			staticObjectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(staticObjectInstance);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}