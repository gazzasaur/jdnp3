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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rits.cloning.Cloner;

public class DatabaseManager {
	private Logger logger = LoggerFactory.getLogger(DatabaseManager.class);
	
	private Database database = new Database();
	private List<EventListener> eventListeners = new ArrayList<>();
	private List<DatabaseListener> databaseListeners = new ArrayList<>();
	
	public void setAnalogDatabaseSize(int size) {
		synchronized (database) {
			while (database.getAnalogDataPoints().size() > size) {
				database.removeAnalogDataPoint();
			}
			while (database.getAnalogDataPoints().size() < size) {
				database.addAnalogDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public void setBinaryDatabaseSize(int size) {
		synchronized (database) {
			while (database.getBinaryDataPoints().size() > size) {
				database.removeBinaryDataPoint();
			}
			while (database.getBinaryDataPoints().size() < size) {
				database.addBinaryDataPoint();
			}
		}
		for (DatabaseListener databaseListener : databaseListeners) {
			databaseListener.modelChanged();
		}
	}
	
	public List<AnalogDataPoint> getAnalogDataPoints() {
		return Cloner.standard().deepClone(database.getAnalogDataPoints());
	}
	
	public List<BinaryInputDataPoint> getBinaryDataPoints() {
		return Cloner.standard().deepClone(database.getBinaryDataPoints());
	}
	
	public void setAnalogDataPoint(AnalogDataPoint analogDataPoint) {
		synchronized (database) {
			database.setAnalogDataPoint(Cloner.standard().deepClone(analogDataPoint));
		}
		if (analogDataPoint.getIndex() < database.getAnalogDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(analogDataPoint);
			}
		} else {
			logger.warn("Cannot write analog data point of index: " + analogDataPoint.getIndex());
		}
	}
	
	public void setBinaryDataPoint(BinaryInputDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryDataPoint(Cloner.standard().deepClone(binaryDataPoint));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		} else {
			logger.warn("Cannot write binary data point of index: " + binaryDataPoint.getIndex());
		}
	}

	public void triggerAnalogEvent(long index) {
		AnalogDataPoint analogDataPoint = database.getAnalogDataPoints().get((int) index);
		for (EventListener eventListener : eventListeners) {
			eventListener.eventReceived(analogDataPoint);
		}
	}

	public void triggerBinaryEvent(long index) {
		BinaryInputDataPoint binaryDataPoint = database.getBinaryDataPoints().get((int) index);
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
