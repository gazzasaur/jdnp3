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

import net.sf.jdnp3.dnp3.service.outstation.handler.binary.DoubleBitBinaryInputStaticAssignClassRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.handler.binary.DoubleBitBinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.DoubleBitBinaryInputStaticObjectInstance;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;

public class DoubleBitBinaryInputStaticHandler implements DoubleBitBinaryInputStaticReadRequestHandler, DoubleBitBinaryInputStaticAssignClassRequestHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(DoubleBitBinaryInputStaticHandler.class);
	
	private DatabaseManager databaseManager;

	public DoubleBitBinaryInputStaticHandler(DatabaseManager databaseManager) {
		this.databaseManager = databaseManager;
	}
	
	public List<DoubleBitBinaryInputStaticObjectInstance> readStatics(long startIndex, long stopIndex) {
		List<DoubleBitBinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();

		for (DoubleBitBinaryInputDataPoint dataPoint : binaryDataPoints) {
			if (dataPoint.getIndex() >= startIndex && dataPoint.getIndex() <= stopIndex) {
				copyDataPoint(points, dataPoint);
			}
		}
		return points;
	}

	public List<DoubleBitBinaryInputStaticObjectInstance> readStatics() {
		List<DoubleBitBinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();

		for (DoubleBitBinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			copyDataPoint(points, binaryDataPoint);
		}
		return points;
	}

	public List<DoubleBitBinaryInputStaticObjectInstance> readStatic(long index) {
		List<DoubleBitBinaryInputStaticObjectInstance> points = new ArrayList<>();
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();
		binaryDataPoints.stream().filter(dataPoint -> dataPoint.getIndex() == index).findAny().ifPresent(dataPoint -> copyDataPoint(points, dataPoint));
		return points;
	}

	public void assignClass(long index, long eventClass) {
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();

		binaryDataPoints.stream().filter(dataPoint -> dataPoint.getIndex() == index).findAny().ifPresent(dataPoint -> {
			dataPoint.setEventClass((int) eventClass);
			databaseManager.setDoubleBitBinaryInputDataPoint(dataPoint);
		});
	}

	public void assignClasses(long eventClass) {
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();

		for (DoubleBitBinaryInputDataPoint binaryDataPoint : binaryDataPoints) {
			binaryDataPoint.setEventClass((int) eventClass);
			databaseManager.setDoubleBitBinaryInputDataPoint(binaryDataPoint);
		}
	}

	public void assignClasses(long startIndex, long stopIndex, long eventClass) {
		List<DoubleBitBinaryInputDataPoint> binaryDataPoints = databaseManager.getDoubleBitBinaryInputDataPoints();

		for (DoubleBitBinaryInputDataPoint dataPoint : binaryDataPoints) {
			if (dataPoint.getIndex() >= startIndex && dataPoint.getIndex() <= stopIndex) {
				dataPoint.setEventClass((int) eventClass);
				databaseManager.setDoubleBitBinaryInputDataPoint(dataPoint);
			}
		}
	}

	public Class<DoubleBitBinaryInputStaticObjectInstance> getObjectInstanceClass() {
		return DoubleBitBinaryInputStaticObjectInstance.class;
	}
	
	private void copyDataPoint(List<DoubleBitBinaryInputStaticObjectInstance> points, DoubleBitBinaryInputDataPoint dataPoint) {
		DoubleBitBinaryInputStaticObjectInstance binaryInputStaticObjectInstance = new DoubleBitBinaryInputStaticObjectInstance();
		try {
			PropertyUtils.copyProperties(binaryInputStaticObjectInstance, dataPoint);
			binaryInputStaticObjectInstance.setRequestedType(dataPoint.getStaticType());
			points.add(binaryInputStaticObjectInstance);
		} catch (Exception e) {
			LOGGER.error("Cannot copy data point.", e);
		}
	}
}