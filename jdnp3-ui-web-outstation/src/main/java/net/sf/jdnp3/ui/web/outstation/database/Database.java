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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class Database {
	private List<BinaryDataPoint> binaryDataPoints = new ArrayList<>();

	public List<BinaryDataPoint> getBinaryDataPoints() {
		return unmodifiableList(binaryDataPoints);
	}
	
	public void addBinaryDataPoint() {
		BinaryDataPoint binaryDataPoint = new BinaryDataPoint();
		binaryDataPoint.setIndex(binaryDataPoints.size());
		binaryDataPoint.setName("Point " + binaryDataPoints.size());
		binaryDataPoints.add(binaryDataPoint);
	}
	
	public void setBinaryDataPoint(BinaryDataPoint binaryDataPoint) {
		if (binaryDataPoint.getIndex() < binaryDataPoints.size()) {
			binaryDataPoints.set((int) binaryDataPoint.getIndex(), binaryDataPoint);
		}
	}
	
	public void removeBinaryDataPoint() {
		if (binaryDataPoints.size() > 0) {
			binaryDataPoints.remove(binaryDataPoints.size() - 1);
		}
	}
}
