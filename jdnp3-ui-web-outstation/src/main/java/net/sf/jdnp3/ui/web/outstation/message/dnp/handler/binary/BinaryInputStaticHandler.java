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
package net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryInputStaticAssignClassRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.binary.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;

public class BinaryInputStaticHandler implements BinaryInputStaticReadRequestHandler, BinaryInputStaticAssignClassRequestHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(BinaryInputStaticHandler.class);
	
	private DatabaseManager databaseManager;

	public BinaryInputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<BinaryInputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (BinaryInputDataPoint dataPoint : binaryDataPoints) {
			if (dataPoint.getIndex() >= startIndex && dataPoint.getIndex() <= stopIndex) {
				copyDataPoint(points, dataPoint);
			}
		}
		return points;
	}

	public List<BinaryInputStaticObjectInstance> readStatics() {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			copyDataPoint(points, binaryDataPoint);
		}
		return points;
	}

	// FIXME All of these are very inefficient across all types.
	public List<BinaryInputStaticObjectInstance> readStatic(long index) {
		List<BinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		binaryDataPoints.stream().filter(dataPoint -> dataPoint.getIndex() == index).findAny().ifPresent(dataPoint -> copyDataPoint(points, dataPoint));
		return points;
	}

	public void assignClass(long index) {
		long eventClass = databaseManager.getAssignClassCategory();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		binaryDataPoints.stream().filter(dataPoint -> dataPoint.getIndex() == index).findAny().ifPresent(dataPoint -> {
			dataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryInputDataPoint(dataPoint);
		});
	}

	public void assignClasses() {
		long eventClass = databaseManager.getAssignClassCategory();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();
		for (BinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			binaryDataPoint.setEventClass((int) eventClass);
			databaseManager.setBinaryInputDataPoint(binaryDataPoint);
		}
	}

	public void assignClasses(long startIndex, long stopIndex) {
		long eventClass = databaseManager.getAssignClassCategory();
		List<BinaryInputDataPoint> binaryDataPoints = databaseManager.getBinaryInputDataPoints();

		for (BinaryInputDataPoint dataPoint : binaryDataPoints) {
			if (dataPoint.getIndex() >= startIndex && dataPoint.getIndex() <= stopIndex) {
				dataPoint.setEventClass((int) eventClass);
				databaseManager.setBinaryInputDataPoint(dataPoint);
			}
		}
	}

	public Class<BinaryInputStaticObjectInstance> getObjectInstanceClass() {
		return BinaryInputStaticObjectInstance.class;
	}
	
	private void copyDataPoint(List<BinaryInputStaticObjectInstance> points, BinaryInputDataPoint dataPoint) {
		BinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new BinaryInputStaticObjectInstance();
		try {
			PropertyUtils.copyProperties(binaryInputStaticObjectInstance, dataPoint);
			binaryInputStaticObjectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(binaryInputStaticObjectInstance);
		} catch (Exception e) {
			LOGGER.error("Cannot copy data point.", e);
		}
	}
}