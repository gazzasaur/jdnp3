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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;

public class Database {
	private InternalIndicatorsDataPoint indicatorsDataPoint = new InternalIndicatorsDataPoint();
	private SortedMap<Long, AnalogInputDataPoint> analogInputDataPoints = new TreeMap<>();
	private SortedMap<Long, BinaryInputDataPoint> binaryInputDataPoints = new TreeMap<>();
	private SortedMap<Long, DoubleBitBinaryInputDataPoint> doubleBitBinaryInputDataPoints = new TreeMap<>();
	private SortedMap<Long, AnalogOutputDataPoint> analogOutputDataPoints = new TreeMap<>();
	private SortedMap<Long, BinaryOutputDataPoint> binaryOutputDataPoints = new TreeMap<>();
	private SortedMap<Long, CounterDataPoint> counterDataPoints = new TreeMap<>();

	public InternalIndicatorsDataPoint getIndicatorsDataPoint() {
		return indicatorsDataPoint;
	}

	public List<AnalogInputDataPoint> getAnalogInputDataPoints() {
		return unmodifiableList(new ArrayList<>(analogInputDataPoints.values()));
	}

	public List<BinaryInputDataPoint> getBinaryInputDataPoints() {
		return unmodifiableList(new ArrayList<>(binaryInputDataPoints.values()));
	}

	public List<DoubleBitBinaryInputDataPoint> getDoubleBitBinaryInputDataPoints() {
		return unmodifiableList(new ArrayList<>(doubleBitBinaryInputDataPoints.values()));
	}

	public List<AnalogOutputDataPoint> getAnalogOutputDataPoints() {
		return unmodifiableList(new ArrayList<>(analogOutputDataPoints.values()));
	}

	public List<BinaryOutputDataPoint> getBinaryOutputDataPoints() {
		return unmodifiableList(new ArrayList<>(binaryOutputDataPoints.values()));
	}

	public List<CounterDataPoint> getCounterDataPoints() {
		return unmodifiableList(new ArrayList<>(counterDataPoints.values()));
	}

	public AnalogInputDataPoint getAnalogInputDataPoint(long index) {
		var point = analogInputDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public BinaryInputDataPoint getBinaryInputDataPoint(long index) {
		var point = binaryInputDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public DoubleBitBinaryInputDataPoint getDoubleBitBinaryInputDataPoint(long index) {
		var point = doubleBitBinaryInputDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public AnalogOutputDataPoint getAnalogOutputDataPoint(long index) {
		var point = analogOutputDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public BinaryOutputDataPoint getBinaryOutputDataPoint(long index) {
		var point = binaryOutputDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public CounterDataPoint getCounterDataPoint(long index) {
		var point = counterDataPoints.get(index);
		if (point == null) {
			throw new IllegalArgumentException("No point found for index: " + index);
		}
		return point;
	}

	public void setAnalogInputDataPoint(AnalogInputDataPoint point) {
		analogInputDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}
	
	public void setBinaryInputDataPoint(BinaryInputDataPoint point) {
		binaryInputDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}

	public void setDoubleBitBinaryInputDataPoint(DoubleBitBinaryInputDataPoint point) {
		doubleBitBinaryInputDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}

	public void setAnalogOutputDataPoint(AnalogOutputDataPoint point) {
		analogOutputDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}

	public void setBinaryOutputDataPoint(BinaryOutputDataPoint point) {
		binaryOutputDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}

	public void setCounterDataPoint(CounterDataPoint point) {
		counterDataPoints.compute(point.getIndex(), (index, currentPoint) -> point);
	}

	public void clear() {
		binaryInputDataPoints.clear();
		doubleBitBinaryInputDataPoints.clear();
		binaryOutputDataPoints.clear();
		analogInputDataPoints.clear();
		analogOutputDataPoints.clear();
		counterDataPoints.clear();
	}
}
