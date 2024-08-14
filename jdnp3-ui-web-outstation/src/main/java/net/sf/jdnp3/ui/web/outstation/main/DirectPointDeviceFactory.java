/**
 * Copyright 2024 Graeme Farquharson
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
package net.sf.jdnp3.ui.web.outstation.main;

import java.util.List;
import net.sf.jdnp3.dnp3.service.outstation.core.ByteDataOutstationApplicationRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.DoubleBitBinaryInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterEventListener;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogOutputCommandOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.BinaryInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.CrobOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.DoubleBitBinaryInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class0Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class1Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class2Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class3Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.InternalIndicatorWriter;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.TimeAndDateHandler;

public class DirectPointDeviceFactory {
	public static OutstationDevice create(
			String siteName,
			String deviceName,
			int primaryAddress,
			List<BinaryInputDataPoint> binaryInputDataPoints,
			List<BinaryOutputDataPoint> binaryOutputDataPoints,
			List<DoubleBitBinaryInputDataPoint> doubleBitBinaryInputDataPoints,
			List<AnalogInputDataPoint> analogInputDataPoints,
			List<AnalogOutputDataPoint> analogOutputDataPoints,
			List<CounterDataPoint> counterDataPoints) {
		OutstationDevice outstationDevice = new OutstationDevice();
		outstationDevice.setSite(siteName);
		outstationDevice.setDevice(deviceName);
		
		DatabaseManager databaseManager = new DatabaseManager();

		long nextIndex = databaseManager.getBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (BinaryInputDataPoint dataPoint : binaryInputDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setBinaryInputDataPoint(dataPoint);
		}

		nextIndex = databaseManager.getBinaryOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (BinaryOutputDataPoint dataPoint : binaryOutputDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setBinaryOutputDataPoint(dataPoint);
		}

		nextIndex = databaseManager.getDoubleBitBinaryInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (DoubleBitBinaryInputDataPoint dataPoint : doubleBitBinaryInputDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setDoubleBitBinaryInputDataPoint(dataPoint);
		}

		nextIndex = databaseManager.getAnalogInputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (AnalogInputDataPoint dataPoint : analogInputDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setAnalogInputDataPoint(dataPoint);
		}

		nextIndex = databaseManager.getAnalogOutputDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (AnalogOutputDataPoint dataPoint : analogOutputDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setAnalogOutputDataPoint(dataPoint);
		}

		nextIndex = databaseManager.getCounterDataPoints().stream().mapToLong(DataPoint::getIndex).max().orElse(0);
		for (CounterDataPoint dataPoint : counterDataPoints) {
			if (dataPoint.getIndex() >= 0 && dataPoint.getIndex() < nextIndex) {
				throw new IllegalArgumentException("Data points must be created in ascending order.");
			}
			if (dataPoint.getIndex() < 0) {
				dataPoint.setIndex(nextIndex++);
			} else {
				nextIndex = dataPoint.getIndex() + 1;
			}
			databaseManager.setCounterDataPoint(dataPoint);
		}

		OutstationFactory outstationFactory = new OutstationFactory();
		
		outstationFactory.addOutstationApplicationRequestHandler(new ByteDataOutstationApplicationRequestHandler());
		outstationFactory.addStandardObjectTypeEncoders();
		outstationFactory.addStandardObjectTypeDecoders();
		outstationFactory.addStandardObjectFragmentPackers();
		outstationFactory.addStandardItemEnumeratorFactories();
		outstationFactory.addStandardOutstationRequestHandlerAdaptors();
		outstationFactory.setInternalStatusProvider(databaseManager.getInternalStatusProvider());
		
		Outstation outstation = outstationFactory.createOutstation();
		outstation.addRequestHandler(new BinaryInputStaticHandler(databaseManager));
		outstation.addRequestHandler(new AnalogInputStaticHandler(databaseManager));
		outstation.addRequestHandler(new DoubleBitBinaryInputStaticHandler(databaseManager));
		outstation.addRequestHandler(new Class0Reader(databaseManager));
		outstation.addRequestHandler(new Class1Reader());
		outstation.addRequestHandler(new Class2Reader());
		outstation.addRequestHandler(new Class3Reader());
		outstation.addRequestHandler(new CrobOperator(databaseManager));
		outstation.addRequestHandler(new AnalogOutputCommandOperator(databaseManager));
		outstation.addRequestHandler(new TimeAndDateHandler(databaseManager.getInternalStatusProvider()));
		outstation.addRequestHandler(new InternalIndicatorWriter(databaseManager.getInternalStatusProvider()));
		
		databaseManager.addEventListener(new BinaryInputEventListener(outstation));
		databaseManager.addEventListener(new BinaryOutputEventListener(outstation));
		databaseManager.addEventListener(new DoubleBitBinaryInputEventListener(outstation));
		databaseManager.addEventListener(new AnalogInputEventListener(outstation));
		databaseManager.addEventListener(new AnalogOutputEventListener(outstation));
		databaseManager.addEventListener(new CounterEventListener(outstation));
		
		outstation.setPrimaryAddress(primaryAddress);
		outstationDevice.setOutstation(outstation);
		outstationDevice.setDatabaseManager(databaseManager);
		DeviceProvider.registerDevice(outstationDevice);
		
		return outstationDevice;
	}
}
