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

import com.rits.cloning.Cloner;

public class DatabaseManager {
	private Database database = new Database();
	private List<DatabaseListener> databaseListeners = new ArrayList<>();
	
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
	
	public void setBinaryDataPoint(BinaryDataPoint binaryDataPoint) {
		synchronized (database) {
			database.setBinaryDataPoint(Cloner.standard().deepClone(binaryDataPoint));
		}
		if (binaryDataPoint.getIndex() < database.getBinaryDataPoints().size()) {
			for (DatabaseListener databaseListener : databaseListeners) {
				databaseListener.valueChanged(binaryDataPoint);
			}
		}
	}
}
