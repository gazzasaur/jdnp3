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

	public void addAnalogInputDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getAnalogInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				AnalogInputDataPoint dataPoint = new AnalogInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setAnalogInputDataPoint(dataPoint);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addAnalogOutputDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getAnalogOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				AnalogOutputDataPoint dataPoint = new AnalogOutputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setAnalogOutputDataPoint(dataPoint);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addBinaryInputDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				BinaryInputDataPoint dataPoint = new BinaryInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setBinaryInputDataPoint(dataPoint);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addBinaryOutputDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getBinaryOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				BinaryOutputDataPoint dataPoint = new BinaryOutputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setBinaryOutputDataPoint(dataPoint);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}

	public void addDoubleBitBinaryInputDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getDoubleBitBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				DoubleBitBinaryInputDataPoint dataPoint = new DoubleBitBinaryInputDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setDoubleBitBinaryInputDataPoint(dataPoint);
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void addCounterDataPoints(String... names) {
		synchronized (database) {
			long nextIndex = database.getCounterDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(-1);
			for (String name : names) {
				CounterDataPoint dataPoint = new CounterDataPoint();
				dataPoint.setIndex(++nextIndex);
				dataPoint.setName(name);
				database.setCounterDataPoint(dataPoint);
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

	public AnalogInputDataPoint getAnalogInputDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getAnalogInputDataPoint(index), AnalogInputDataPoint.class);
		}
	}

	public List<AnalogOutputDataPoint> getAnalogOutputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getAnalogOutputDataPoints(), AnalogOutputDataPoint.class);
		}
	}

	public AnalogOutputDataPoint getAnalogOutputDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getAnalogOutputDataPoint(index), AnalogOutputDataPoint.class);
		}
	}

	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getBinaryInputDataPoints(), BinaryInputDataPoint.class);
		}
	}

	public BinaryInputDataPoint getBinaryInputDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getBinaryInputDataPoint(index), BinaryInputDataPoint.class);
		}
	}

	public List<DoubleBitBinaryInputDataPoint> getDoubleBitBinaryInputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getDoubleBitBinaryInputDataPoints(), DoubleBitBinaryInputDataPoint.class);
		}
	}

	public DoubleBitBinaryInputDataPoint getDoubleBitBinaryInputDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getDoubleBitBinaryInputDataPoint(index), DoubleBitBinaryInputDataPoint.class);
		}
	}

	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getBinaryOutputDataPoints(), BinaryOutputDataPoint.class);
		}
	}

	public BinaryOutputDataPoint getBinaryOutputDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getBinaryOutputDataPoint(index), BinaryOutputDataPoint.class);
		}
	}

	public List<CounterDataPoint> getCounterDataPoints() {
		synchronized (database) {
			return this.cloneObjects(database.getCounterDataPoints(), CounterDataPoint.class);
		}
	}

	public CounterDataPoint getCounterDataPoint(long index) {
		synchronized (database) {
			return this.cloneObject(database.getCounterDataPoint(index), CounterDataPoint.class);
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
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.valueChanged(analogDataPoint);
		}
		if (analogDataPoint.isTriggerEventOnChange()) {
			this.triggerAnalogInputEvent(analogDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setAnalogOutputDataPoint(AnalogOutputDataPoint analogDataPoint) {
		synchronized (database) {
			database.setAnalogOutputDataPoint(this.cloneObject(analogDataPoint, AnalogOutputDataPoint.class));
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.valueChanged(analogDataPoint);
		}
		if (analogDataPoint.isTriggerEventOnChange()) {
			this.triggerAnalogOutputEvent(analogDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryInputDataPoint(this.cloneObject(binaryDataPoint, BinaryInputDataPoint.class));
		}
		for (DatabaseListener databaseListener : databaseListeners) {
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
		synchronized (database) {
			database.setDoubleBitBinaryInputDataPoint(this.cloneObject(binaryDataPoint, DoubleBitBinaryInputDataPoint.class));
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		if (binaryDataPoint.isTriggerEventOnChange()) {
			this.triggerDoubleBitBinaryInputEvent(binaryDataPoint.getIndex(), System.currentTimeMillis());
		}
	}

	public void setBinaryOutputDataPoint(BinaryOutputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryOutputDataPoint(this.cloneObject(binaryDataPoint, BinaryOutputDataPoint.class));
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.valueChanged(binaryDataPoint);
		}
		if (binaryDataPoint.isTriggerEventOnChange()) {
			this.triggerBinaryOutputEvent(binaryDataPoint.getIndex(), System.currentTimeMillis());
		}
	}
	
	public void setCounterDataPoint(CounterDataPoint dataPoint) {
		synchronized (database) {
			database.setCounterDataPoint(this.cloneObject(dataPoint, CounterDataPoint.class));
		}
		for (DatabaseListener databaseListener : databaseListeners) {
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
