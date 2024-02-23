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

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class DatabaseManager {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	
	private Database database = new Database();
	private List<EventListener> eventListeners = new ArrayList<>();
	private List<DatabaseListener> databaseListeners = new ArrayList<>();
	private InternalStatusProvider internalStatusProvider = new DatabaseInternalIndicatorProvider(this);

	public InternalStatusProvider getInternalStatusProvider() {
		return internalStatusProvider;
	}

	public void setAnalogInputDatabaseSize(int size) {
		synchronized (database) {
			while (database.getAnalogInputDataPoints().size() > size) {
				database.removeAnalogInputDataPoint();
			}
			while (database.getAnalogInputDataPoints().size() < size) {
				database.addAnalogInputDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addAnalogInputDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addAnalogInputDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void setAnalogOutputDatabaseSize(int size) {
		synchronized (database) {
			while (database.getAnalogOutputDataPoints().size() > size) {
				database.removeAnalogOutputDataPoint();
			}
			while (database.getAnalogOutputDataPoints().size() < size) {
				database.addAnalogOutputDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addAnalogOutputDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addAnalogOutputDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void setBinaryInputDatabaseSize(int size) {
		synchronized (database) {
			while (database.getBinaryInputDataPoints().size() > size) {
				database.removeBinaryInputDataPoint();
			}
			while (database.getBinaryInputDataPoints().size() < size) {
				database.addBinaryInputDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addBinaryInputDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addBinaryInputDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void setBinaryOutputDatabaseSize(int size) {
		synchronized (database) {
			while (database.getBinaryOutputDataPoints().size() > size) {
				database.removeBinaryOutputDataPoint();
			}
			while (database.getBinaryOutputDataPoints().size() < size) {
				database.addBinaryOutputDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addBinaryOutputDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addBinaryOutputDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void setDoubleBitBinaryInputDatabaseSize(int size) {
		synchronized (database) {
			while (database.getDoubleBitBinaryInputDataPoints().size() > size) {
				database.removeDoubleBitBinaryInputDataPoint();
			}
			while (database.getDoubleBitBinaryInputDataPoints().size() < size) {
				database.addDoubleBitBinaryInputDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addDoubleBitBinaryInputDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addDoubleBitBinaryInputDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void setCounterDatabaseSize(int size) {
		synchronized (database) {
			while (database.getCounterDataPoints().size() > size) {
				database.removeCounterDataPoint();
			}
			while (database.getCounterDataPoints().size() < size) {
				database.addCounterDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addCounterDataPoints(String... names) {
		synchronized (database) {
			for (String name : names) {
				database.addCounterDataPoint(name);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public InternalIndicatorsDataPoint getInternalIndicatorsDataPoint() {
		synchronized (database) {
			return this.cloneObject(database.getIndicatorsDataPoint(), InternalIndicatorsDataPoint.class);
		}
	}
	
	public List<AnalogInputDataPoint> getAnalogInputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getAnalogInputDataPoints(), AnalogInputDataPoint.class);
		}
	}
	
	public List<AnalogOutputDataPoint> getAnalogOutputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getAnalogOutputDataPoints(), AnalogOutputDataPoint.class);
		}
	}
	
	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getBinaryInputDataPoints(), BinaryInputDataPoint.class);
		}
	}

	public List<DoubleBitBinaryInputDataPoint> getDoubleBitBinaryInputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getDoubleBitBinaryInputDataPoints(), DoubleBitBinaryInputDataPoint.class);
		}
	}

	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getBinaryOutputDataPoints(), BinaryOutputDataPoint.class);
		}
	}
	
	public List<CounterDataPoint> getCounterDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getCounterDataPoints(), CounterDataPoint.class);
		}
	}
	
	public void setInternalIndicatorDataPoint(InternalIndicatorsDataPoint dataPoint) {
		try {
			synchronized (database) {
				BeanUtils.copyProperties(database.getIndicatorsDataPoint(), dataPoint);
			}
		} catch (Exception e) {
			logger.error("Cannot set IIN bits.", e);
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.valueChanged(dataPoint);
		}
	}
	
	public void setAnalogInputDataPoint(AnalogInputDataPoint analogDataPoint) {
		synchronized (database) {
			database.setAnalogInputDataPoint(this.cloneObject(analogDataPoint, AnalogInputDataPoint.class));
		}
		if (analogDataPoint.getIndex() < database.getAnalogInputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(database.getAnalogInputDataPoints().get((int) analogDataPoint.getIndex()));
			}
		} else {
			logger.warn("Cannot write analog data point of index: " + analogDataPoint.getIndex());
		}
	}
	
	public void setAnalogOutputDataPoint(AnalogOutputDataPoint analogDataPoint) {
		synchronized (database) {
			database.setAnalogOutputDataPoint(this.cloneObject(analogDataPoint, AnalogOutputDataPoint.class));
		}
		if (analogDataPoint.getIndex() < database.getAnalogOutputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(analogDataPoint);
			}
		} else {
			logger.warn("Cannot write analog data point of index: " + analogDataPoint.getIndex());
		}
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryInputDataPoint(this.cloneObject(binaryDataPoint, BinaryInputDataPoint.class));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryInputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write binary data point of index: " + binaryDataPoint.getIndex());
		}
	}

	public void setDoubleBitBinaryInputDataPoint(DoubleBitBinaryInputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setDoubleBitBinaryInputDataPoint(this.cloneObject(binaryDataPoint, DoubleBitBinaryInputDataPoint.class));
		}
		if (binaryDataPoint.getIndex() < database.getDoubleBitBinaryInputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write double bit binary data point of index: " + binaryDataPoint.getIndex());
		}
	}

	public void setBinaryOutputDataPoint(BinaryOutputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryOutputDataPoint(this.cloneObject(binaryDataPoint, BinaryOutputDataPoint.class));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryOutputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write binary output data point of index: " + binaryDataPoint.getIndex());
		}
	}
	
	public void setCounterDataPoint(CounterDataPoint dataPoint) {
		synchronized (database) {
			database.setCounterDataPoint(this.cloneObject(dataPoint, CounterDataPoint.class));
		}
		if (dataPoint.getIndex() < database.getCounterDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(dataPoint);
			}
		} else {
			logger.warn("Cannot write data point of index: " + dataPoint.getIndex());
		}
	}

	public void triggerAnalogInputEvent(long index, long timestamp) {
		AnalogInputDataPoint analogDataPoint = database.getAnalogInputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(analogDataPoint, timestamp);
		}
	}
	
	public void triggerAnalogOutputEvent(long index, long timestamp) {
		AnalogOutputDataPoint dataPoint = database.getAnalogOutputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(dataPoint, timestamp);
		}
	}

	public void triggerBinaryInputEvent(long index, long timestamp) {
		BinaryInputDataPoint binaryDataPoint = database.getBinaryInputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerDoubleBitBinaryInputEvent(long index, long timestamp) {
		DoubleBitBinaryInputDataPoint binaryDataPoint = database.getDoubleBitBinaryInputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerBinaryOutputEvent(long index, long timestamp) {
		BinaryOutputDataPoint binaryDataPoint = database.getBinaryOutputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint, timestamp);
		}
	}

	public void triggerCounterEvent(long index, long timestamp) {
		CounterDataPoint counterDataPoint = database.getCounterDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(counterDataPoint, timestamp);
		}
	}

	public void clear() {
		synchronized (database) {
			database.clear();
		}
		for (DatabaseListener databaseListener : databaseListeners) {
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

	private <T> T cloneObject(T obj, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(obj), clazz);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}

	private <T> List<T> cloneObjects(List<T> obj, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(obj), OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
