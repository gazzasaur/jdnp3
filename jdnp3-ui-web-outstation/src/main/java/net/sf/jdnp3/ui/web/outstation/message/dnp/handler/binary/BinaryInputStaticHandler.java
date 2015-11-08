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

import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryInputStaticAssignClassRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;

import org.apache.commons.beanutils.BeanUtils;

public class BinaryInputStaticHandler implements BinaryInputStaticReadRequestHandler, BinaryInputStaticAssignClassRequestHandler {
	private DatabaseManager databaseManager;

	public BinaryInputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<BinaryInputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoints.get((int) i));
				points.add(binaryInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<BinaryInputStaticObjectInstance> readStatics() {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoint);
				points.add(binaryInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public List<BinaryInputStaticObjectInstance> readStatic(long index) {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		if (index < binaryDataPoints.size()) {
			BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
			try {
				BeanUtils.copyProperties(binaryInputStaticObjectInstance, binaryDataPoints.get((int) index));
				points.add(binaryInputStaticObjectInstance);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return points;
	}

	public void assignClass(long index, long eventClass) {
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		if (index < binaryDataPoints.size()) {
			BinaryInputDataPoint point = binaryDataPoints.get((int) index);
			point.setEventClass((int) eventClass);
			databaseManager.setBinaryInputDataPoint(point);
		}
	}

	public void assignClasses(long eventClass) {
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			binaryDataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryInputDataPoint(binaryDataPoint);
		}
	}

	public void assignClasses(long startIndex, long stopIndex, long eventClass) {
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (long i = startIndex; i <= stopIndex; ++i) {
			BinaryInputDataPoint binaryDataPoint = binaryDataPoints.get((int) i);
			binaryDataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryInputDataPoint(binaryDataPoint);
		}
	}

	public Class<BinaryInputStaticObjectInstance> getObjectInstanceClass() {
		return BinaryInputStaticObjectInstance.class;
	}
}