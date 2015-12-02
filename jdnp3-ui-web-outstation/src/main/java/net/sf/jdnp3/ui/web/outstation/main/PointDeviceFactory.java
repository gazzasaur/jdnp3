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
package net.sf.jdnp3.ui.web.outstation.main;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.service.outstation.core.ByteDataOutstationApplicationRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ByteDataObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.IndexPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.LengthPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.CountRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VariableFormatQualifierRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.VirtualAddressRange;
import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.analog.AnalogOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputDataPoint;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryOutputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.point.counter.CounterDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.analog.AnalogOutputCommandOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.BinaryInputStaticHandler;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.binary.CrobOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class0Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class1Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class2Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.Class3Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.InternalIndicatorWriter;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.generic.TimeAndDateHandler;

public class PointDeviceFactory implements DeviceFactory {
	private List<BinaryInputDataPoint> binaryInputDataPoints = new ArrayList<>();
	private List<BinaryOutputDataPoint> binaryOutputDataPoints = new ArrayList<>();
	private List<AnalogInputDataPoint> analogInputDataPoints = new ArrayList<>();
	private List<AnalogOutputDataPoint> analogOutputDataPoints = new ArrayList<>();
	private List<CounterDataPoint> counterDataPoints = new ArrayList<>();
	
	@SuppressWarnings("serial")
	private Map<String, Class<? extends Range>> rangeClassMapping = new HashMap<String, Class<? extends Range>>() {{
		this.put("NoRange", NoRange.class);
		this.put("CountRange", CountRange.class);
		this.put("IndexRange", IndexRange.class);
		this.put("VirtualAddressRange", VirtualAddressRange.class);
		this.put("VariableFormatQualifierRange", VariableFormatQualifierRange.class);
	}};

	@SuppressWarnings("serial")
	private Map<String, Class<? extends PrefixType>> prefixTypeClassMapping = new HashMap<String, Class<? extends PrefixType>>() {{
		this.put("NoPrefixType", NoPrefixType.class);
		this.put("IndexPrefixType", IndexPrefixType.class);
		this.put("LengthPrefixType", LengthPrefixType.class);
	}};

	public OutstationDevice create(String siteName, String deviceName, int primaryAddress) {
		return this.create(siteName, deviceName, primaryAddress, new ExtendedConfiguration());
	}

	// FIXME Bug will not allow correct naming and indexing of points.
	public OutstationDevice create(String siteName, String deviceName, int primaryAddress, ExtendedConfiguration extendedConfiguration) {
		OutstationDevice outstationDevice = new OutstationDevice();
		outstationDevice.setSite(siteName);
		outstationDevice.setDevice(deviceName);
		
		DatabaseManager databaseManager = new DatabaseManager();
		databaseManager.setBinaryInputDatabaseSize(binaryInputDataPoints.size());
		for (BinaryInputDataPoint binaryInputDataPoint : binaryInputDataPoints) {
			databaseManager.setBinaryInputDataPoint(binaryInputDataPoint);
		}
		
		databaseManager.setBinaryOutputDatabaseSize(binaryOutputDataPoints.size());
		for (BinaryOutputDataPoint binaryOutputDataPoint : binaryOutputDataPoints) {
			databaseManager.setBinaryOutputDataPoint(binaryOutputDataPoint);
		}

		databaseManager.setAnalogInputDatabaseSize(analogInputDataPoints.size());
		for (AnalogInputDataPoint analogInputDataPoint : analogInputDataPoints) {
			databaseManager.setAnalogInputDataPoint(analogInputDataPoint);
		}

		databaseManager.setAnalogOutputDatabaseSize(analogOutputDataPoints.size());
		for (AnalogOutputDataPoint analogOutputDataPoint : analogOutputDataPoints) {
			databaseManager.setAnalogOutputDataPoint(analogOutputDataPoint);
		}

		databaseManager.setCounterDatabaseSize(counterDataPoints.size());
		for (CounterDataPoint counterDataPoint : counterDataPoints) {
			databaseManager.setCounterDataPoint(counterDataPoint);
		}
		
		databaseManager.addBinaryInputDataPoints(extendedConfiguration.getBinaryInputPoints().toArray(new String[0]));
		databaseManager.addBinaryOutputDataPoints(extendedConfiguration.getBinaryOutputPoints().toArray(new String[0]));
		databaseManager.addAnalogInputDataPoints(extendedConfiguration.getAnalogInputPoints().toArray(new String[0]));
		databaseManager.addAnalogOutputDataPoints(extendedConfiguration.getAnalogOutputPoints().toArray(new String[0]));
		databaseManager.addCounterDataPoints(extendedConfiguration.getCounterPoints().toArray(new String[0]));
		
		OutstationFactory outstationFactory = new OutstationFactory();
		for (CustomType customType : extendedConfiguration.getCustomTypes()) {
			Class<? extends Range> rangeClass = rangeClassMapping.get(customType.getRangeClass());
			if (rangeClass == null) {
				throw new IllegalArgumentException("No range found for requested type " + customType.getRangeClass());
			}
			
			Class<? extends PrefixType> prefixTypeClass = prefixTypeClassMapping.get(customType.getPrefixType());
			if (prefixTypeClass == null) {
				throw new IllegalArgumentException("No prefix type found for requested type " + customType.getPrefixType());
			}
			
			outstationFactory.addCustomDecoder(new ByteDataObjectTypeDecoder(customType.getFunctionCode(), customType.getExpectedData(), customType.getResponseData()));
		}
		
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
		databaseManager.addEventListener(new AnalogInputEventListener(outstation));
		
		outstation.setPrimaryAddress(primaryAddress);
		outstationDevice.setOutstation(outstation);
		outstationDevice.setDatabaseManager(databaseManager);
		DeviceProvider.registerDevice(outstationDevice);
		
		return outstationDevice;
	}

	public void setBinaryInputDataPoints(BinaryInputDataPoint... binaryInputDataPoints) {
		this.binaryInputDataPoints = asList(binaryInputDataPoints);
	}

	public void setBinaryOutputDataPoints(BinaryOutputDataPoint... binaryOutputDataPoints) {
		this.binaryOutputDataPoints = asList(binaryOutputDataPoints);
	}

	public void setAnalogInputDataPoints(AnalogInputDataPoint... analogInputDataPoints) {
		this.analogInputDataPoints = asList(analogInputDataPoints);
	}

	public void setAnalogOutputDataPoints(AnalogOutputDataPoint... analogOutputDataPoints) {
		this.analogOutputDataPoints = asList(analogOutputDataPoints);
	}

	public void setCounterDataPoints(CounterDataPoint... counterDataPoints) {
		this.counterDataPoints = asList(counterDataPoints);
	}
}