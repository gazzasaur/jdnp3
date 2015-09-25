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
package net.sf.jdnp3.ui.web.outstation.database;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public class DatabaseManager {
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
	
	public InternalIndicatorsDataPoint getInternalIndicatorsDataPoint() {
		synchronized (database) {
			return Cloner.standard().deepClone(database.getIndicatorsDataPoint());
		}
	}
	
	public List<AnalogInputDataPoint> getAnalogInputDataPoints() {
		synchronized (database) {
			return Cloner.standard().deepClone(database.getAnalogInputDataPoints());
		}
	}
	
	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		synchronized (database) {
			return Cloner.standard().deepClone(database.getBinaryInputDataPoints());
		}
	}
	
	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		synchronized (database) {
			return Cloner.standard().deepClone(database.getBinaryOutputDataPoints());
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
			database.setAnalogInputDataPoint(Cloner.standard().deepClone(analogDataPoint));
		}
		if (analogDataPoint.getIndex() < database.getAnalogInputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(analogDataPoint);
			}
		} else {
			logger.warn("Cannot write analog data point of index: " + analogDataPoint.getIndex());
		}
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryInputDataPoint(Cloner.standard().deepClone(binaryDataPoint));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryInputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write binary data point of index: " + binaryDataPoint.getIndex());
		}
	}
	
	public void setBinaryOutputDataPoint(BinaryOutputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryOutputDataPoint(Cloner.standard().deepClone(binaryDataPoint));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryOutputDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write binary output data point of index: " + binaryDataPoint.getIndex());
		}
	}

	public void triggerAnalogInputEvent(long index) {
		AnalogInputDataPoint analogDataPoint = database.getAnalogInputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(analogDataPoint);
		}
	}

	public void triggerBinaryInputEvent(long index) {
		BinaryInputDataPoint binaryDataPoint = database.getBinaryInputDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(binaryDataPoint);
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
