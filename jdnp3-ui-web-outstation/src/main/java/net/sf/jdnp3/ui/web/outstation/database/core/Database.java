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
package net.sf.jdnp3.ui.web.outstation.database.core;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;

public class Database {
	private InternalIndicatorsDataPoint indicatorsDataPoint = new InternalIndicatorsDataPoint();
	private List<AnalogInputDataPoint> analogInputDataPoints = new ArrayList<>();
	private List<BinaryInputDataPoint> binaryInputDataPoints = new ArrayList<>();
	private List<BinaryOutputDataPoint> binaryOutputDataPoints = new ArrayList<>();

	public InternalIndicatorsDataPoint getIndicatorsDataPoint() {
		return indicatorsDataPoint;
	}

	public List<AnalogInputDataPoint> getAnalogInputDataPoints() {
		return unmodifiableList(analogInputDataPoints);
	}

	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		return unmodifiableList(binaryInputDataPoints);
	}

	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		return unmodifiableList(binaryOutputDataPoints);
	}
	
	public void addAnalogInputDataPoint() {
		AnalogInputDataPoint analogDataPoint = new AnalogInputDataPoint();
		analogDataPoint.setIndex(analogInputDataPoints.size());
		analogDataPoint.setName("Point " + analogInputDataPoints.size());
		analogInputDataPoints.add(analogDataPoint);
	}
	
	public void addAnalogInputDataPoint(String name) {
		AnalogInputDataPoint analogDataPoint = new AnalogInputDataPoint();
		analogDataPoint.setIndex(analogInputDataPoints.size());
		analogDataPoint.setName(name);
		analogInputDataPoints.add(analogDataPoint);
	}
	
	public void addBinaryInputDataPoint() {
		BinaryInputDataPoint binaryDataPoint = new BinaryInputDataPoint();
		binaryDataPoint.setIndex(binaryInputDataPoints.size());
		binaryDataPoint.setName("Point " + binaryInputDataPoints.size());
		binaryInputDataPoints.add(binaryDataPoint);
	}
	
	public void addBinaryInputDataPoint(String name) {
		BinaryInputDataPoint binaryDataPoint = new BinaryInputDataPoint();
		binaryDataPoint.setIndex(binaryInputDataPoints.size());
		binaryDataPoint.setName(name);
		binaryInputDataPoints.add(binaryDataPoint);
	}
	
	public void addBinaryOutputDataPoint() {
		BinaryOutputDataPoint binaryDataPoint = new BinaryOutputDataPoint();
		binaryDataPoint.setIndex(binaryOutputDataPoints.size());
		binaryDataPoint.setName("Point " + binaryOutputDataPoints.size());
		binaryOutputDataPoints.add(binaryDataPoint);
	}
	
	public void addBinaryOutputDataPoint(String name) {
		BinaryOutputDataPoint binaryDataPoint = new BinaryOutputDataPoint();
		binaryDataPoint.setIndex(binaryOutputDataPoints.size());
		binaryDataPoint.setName(name);
		binaryOutputDataPoints.add(binaryDataPoint);
	}
	
	public void setAnalogInputDataPoint(AnalogInputDataPoint analogInputDataPoint) {
		if (analogInputDataPoint.getIndex() < analogInputDataPoints.size()) {
			analogInputDataPoint.setName(analogInputDataPoints.get((int) analogInputDataPoint.getIndex()).getName());
			analogInputDataPoints.set((int) analogInputDataPoint.getIndex(), analogInputDataPoint);
		}
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint binaryInputDataPoint) {
		if (binaryInputDataPoint.getIndex() < binaryInputDataPoints.size()) {
			binaryInputDataPoint.setName(binaryInputDataPoints.get((int) binaryInputDataPoint.getIndex()).getName());
			binaryInputDataPoints.set((int) binaryInputDataPoint.getIndex(), binaryInputDataPoint);
		}
	}
	
	public void setBinaryOutputDataPoint(BinaryOutputDataPoint binaryDataPoint) {
		if (binaryDataPoint.getIndex() < binaryInputDataPoints.size()) {
			binaryDataPoint.setName(binaryInputDataPoints.get((int) binaryDataPoint.getIndex()).getName());
			binaryOutputDataPoints.set((int) binaryDataPoint.getIndex(), binaryDataPoint);
		}
	}
	
	public void removeAnalogInputDataPoint() {
		if (analogInputDataPoints.size() > 0) {
			analogInputDataPoints.remove(analogInputDataPoints.size() - 1);
		}
	}
	
	public void removeBinaryInputDataPoint() {
		if (binaryInputDataPoints.size() > 0) {
			binaryInputDataPoints.remove(binaryInputDataPoints.size() - 1);
		}
	}
	
	public void removeBinaryOutputDataPoint() {
		if (binaryOutputDataPoints.size() > 0) {
			binaryOutputDataPoints.remove(binaryOutputDataPoints.size() - 1);
		}
	}

	public void clear() {
		binaryInputDataPoints.clear();
		binaryOutputDataPoints.clear();
		analogInputDataPoints.clear();
	}
}
