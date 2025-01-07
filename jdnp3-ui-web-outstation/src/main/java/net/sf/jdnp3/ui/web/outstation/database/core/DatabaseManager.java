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
package net.sf.jdnp3.ui.web.outstation.database.core;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class DatabaseManager {
	private static final Logger LOGGER = LoggerFactory.getLogger(DatabaseManager.class);
	
	private Database database = new Database();
	private List<EventListener> eventListeners = new ArrayList<>();
	private List<DatabaseListener> databaseListeners = new ArrayList<>();
	private InternalStatusProvider internalStatusProvider = new DatabaseInternalIndicatorProvider(this);

	public InternalStatusProvider getInternalStatusProvider() {
		return internalStatusProvider;
	}

	public void addAnalogInputDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getAnalogInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				AnalogInputDataPoint dataPoint = new AnalogInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setAnalogInputDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addAnalogOutputDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getAnalogOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				AnalogOutputDataPoint dataPoint = new AnalogOutputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setAnalogOutputDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addBinaryInputDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				BinaryInputDataPoint dataPoint = new BinaryInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setBinaryInputDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addBinaryOutputDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getBinaryOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				BinaryOutputDataPoint dataPoint = new BinaryOutputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setBinaryOutputDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addDoubleBitBinaryInputDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getDoubleBitBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				DoubleBitBinaryInputDataPoint dataPoint = new DoubleBitBinaryInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setDoubleBitBinaryInputDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addCounterDataPoints(String... names) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			long nextIndex = database.getCounterDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				CounterDataPoint dataPoint = new CounterDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setCounterDataPoint(dataPoint);
			}
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public InternalIndicatorsDataPoint getInternalIndicatorsDataPoint() {
		synchronized (database) {
			return database.getIndicatorsDataPoint().copy();
		}
	}
	
	public List<AnalogInputDataPoint> getAnalogInputDataPoints() {
		synchronized (database) {
			List<AnalogInputDataPoint> points = new ArrayList<>();
			for (AnalogInputDataPoint point : database.getAnalogInputDataPoints()) {
				points.add(point.copy());
			}
			return points;
		}
	}

	public AnalogInputDataPoint getAnalogInputDataPoint(long index) {
		synchronized (database) {
			return database.getAnalogInputDataPoint(index).copy();
		}
	}

	public List<AnalogOutputDataPoint> getAnalogOutputDataPoints() {
		synchronized (database) {
			List<AnalogOutputDataPoint> points = new ArrayList<>();
			for (AnalogOutputDataPoint point : database.getAnalogOutputDataPoints()) {
				points.add(point);
			}
			return points;
		}
	}

	public AnalogOutputDataPoint getAnalogOutputDataPoint(long index) {
		synchronized (database) {
			return database.getAnalogOutputDataPoint(index).copy();
		}
	}

	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		synchronized (database) {
			List<BinaryInputDataPoint> points = new ArrayList<>();
			for (BinaryInputDataPoint point : database.getBinaryInputDataPoints()) {
				points.add(point);
			}
			return points;
		}
	}

	public BinaryInputDataPoint getBinaryInputDataPoint(long index) {
		synchronized (database) {
			return database.getBinaryInputDataPoint(index).copy();
		}
	}

	public List<DoubleBitBinaryInputDataPoint> getDoubleBitBinaryInputDataPoints() {
		List<DoubleBitBinaryInputDataPoint> points = new ArrayList<>();
		for (DoubleBitBinaryInputDataPoint point : database.getDoubleBitBinaryInputDataPoints()) {
			points.add(point);
		}
		return points;
}

	public DoubleBitBinaryInputDataPoint getDoubleBitBinaryInputDataPoint(long index) {
		synchronized (database) {
			return database.getDoubleBitBinaryInputDataPoint(index).copy();
		}
	}

	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		List<BinaryOutputDataPoint> points = new ArrayList<>();
		for (BinaryOutputDataPoint point : database.getBinaryOutputDataPoints()) {
			points.add(point);
		}
		return points;
}

	public BinaryOutputDataPoint getBinaryOutputDataPoint(long index) {
		synchronized (database) {
			return database.getBinaryOutputDataPoint(index).copy();
		}
	}

	public List<CounterDataPoint> getCounterDataPoints() {
		List<CounterDataPoint> points = new ArrayList<>();
		for (CounterDataPoint point : database.getCounterDataPoints()) {
			points.add(point);
		}
		return points;
}

	public CounterDataPoint getCounterDataPoint(long index) {
		synchronized (database) {
			return database.getCounterDataPoint(index).copy();
		}
	}

	public void setInternalIndicatorDataPoint(InternalIndicatorsDataPoint dataPoint) {
		try {
			List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
			synchronized (database) {
				PropertyUtils.copyProperties(database.getIndicatorsDataPoint(), dataPoint);
				snapshotDatabaseListeners.addAll(databaseListeners);
			}
			for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
				databaseListener.valueChanged(dataPoint);
			}
		} catch (Exception e) {
			LOGGER.error("Cannot set IIN bits.", e);
		}
	}
	
	public void setAnalogInputDataPoint(AnalogInputDataPoint analogDataPoint) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setAnalogInputDataPoint(analogDataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(analogDataPoint);
		}
		if (analogDataPoint.isTriggerEventOnChange()) {
			this.triggerAnalogInputEvent(analogDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setAnalogOutputDataPoint(AnalogOutputDataPoint analogDataPoint) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setAnalogOutputDataPoint(analogDataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(analogDataPoint);
		}
		if (analogDataPoint.isTriggerEventOnChange()) {
			this.triggerAnalogOutputEvent(analogDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint binaryDataPoint) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setBinaryInputDataPoint(binaryDataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		if (binaryDataPoint.isTriggerEventOnChange()) {
			this.triggerBinaryInputEvent(binaryDataPoint.getIndex(), System.currentTimeMillis());
		}
	}

	public void setDoubleBitBinaryInputDataPoint(DoubleBitBinaryInputDataPoint binaryDataPoint) {
		if ((binaryDataPoint.getValue()) < 0 || (binaryDataPoint.getValue() > 3)) {
			throw new IllegalArgumentException("Unsupported Double Bit Binary Value: " + binaryDataPoint.getValue());
		}
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setDoubleBitBinaryInputDataPoint(binaryDataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		if (binaryDataPoint.isTriggerEventOnChange()) {
			this.triggerDoubleBitBinaryInputEvent(binaryDataPoint.getIndex(), System.currentTimeMillis());
		}
	}

	public void setBinaryOutputDataPoint(BinaryOutputDataPoint binaryDataPoint) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setBinaryOutputDataPoint(binaryDataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		if (binaryDataPoint.isTriggerEventOnChange()) {
			this.triggerBinaryOutputEvent(binaryDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setCounterDataPoint(CounterDataPoint dataPoint) {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.setCounterDataPoint(dataPoint.copy());
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.valueChanged(dataPoint);
		}
		if (dataPoint.isTriggerEventOnChange()) {
			this.triggerCounterEvent(dataPoint.getIndex(), System.currentTimeMillis());
		}
	}

	public void triggerAnalogInputEvent(long index, long timestamp) {
		AnalogInputDataPoint analogDataPoint = database.getAnalogInputDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(analogDataPoint, timestamp);
		}
	}
	
	public void triggerAnalogOutputEvent(long index, long timestamp) {
		AnalogOutputDataPoint dataPoint = database.getAnalogOutputDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(dataPoint, timestamp);
		}
	}

	public void triggerBinaryInputEvent(long index, long timestamp) {
		BinaryInputDataPoint binaryDataPoint = database.getBinaryInputDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerDoubleBitBinaryInputEvent(long index, long timestamp) {
		DoubleBitBinaryInputDataPoint binaryDataPoint = database.getDoubleBitBinaryInputDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerBinaryOutputEvent(long index, long timestamp) {
		BinaryOutputDataPoint binaryDataPoint = database.getBinaryOutputDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerCounterEvent(long index, long timestamp) {
		CounterDataPoint counterDataPoint = database.getCounterDataPoint(index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(counterDataPoint, timestamp);
		}
	}

	public void clear() {
		List<DatabaseListener> snapshotDatabaseListeners = new ArrayList<DatabaseListener>();
		synchronized (database) {
			database.clear();
			snapshotDatabaseListeners.addAll(databaseListeners);
		}
		for (DatabaseListener databaseListener : snapshotDatabaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addDatabaseListener(DatabaseListener databaseListener) {
		databaseListeners.add(databaseListener);
	}

	public void removeDatabaseListener(DatabaseListener databaseListener) {
		databaseListeners.remove(databaseListener);
	}
	
	public void addEventListener(EventListener eventListener) {
		eventListeners.add(eventListener);
	}

	public void removeEventListener(EventListener eventListener) {
		eventListeners.remove(eventListener);
	}
}
