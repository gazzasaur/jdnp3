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
package net.sf.jdnp3.ui.web.outstation.main;

import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

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

public class SimpleDeviceFactory implements DeviceFactory {
	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private List<String> binaryInputDataPoints = new ArrayList<>();
	private List<String> doubleBitBinaryInputDataPoints = new ArrayList<>();
	private List<String> binaryOutputDataPoints = new ArrayList<>();
	private List<String> analogInputDataPoints = new ArrayList<>();
	private List<String> analogOutputDataPoints = new ArrayList<>();
	private List<String> counterDataPoints = new ArrayList<>();
	
	private BinaryInputDataPoint templateBinaryInputDataPoint = new BinaryInputDataPoint();
	private DoubleBitBinaryInputDataPoint templateDoubleBitBinaryInputDataPoint = new DoubleBitBinaryInputDataPoint();
	private BinaryOutputDataPoint templateBinaryOutputDataPoint = new BinaryOutputDataPoint();
	private AnalogInputDataPoint templateAnalogInputDataPoint = new AnalogInputDataPoint();
	private AnalogOutputDataPoint templateAnalogOutputDataPoint = new AnalogOutputDataPoint();
	private CounterDataPoint templateCounterDataPoint = new CounterDataPoint();
	
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

	public OutstationDevice create(String siteName, String deviceName, int primaryAddress, ExtendedConfiguration extendedConfiguration) {
		OutstationDevice outstationDevice = new OutstationDevice();
		outstationDevice.setSite(siteName);
		outstationDevice.setDevice(deviceName);
		
		DatabaseManager databaseManager = new DatabaseManager();
		databaseManager.addBinaryInputDataPoints(binaryInputDataPoints.toArray(new String[0]));
		databaseManager.addDoubleBitBinaryInputDataPoints(doubleBitBinaryInputDataPoints.toArray(new String[0]));
		databaseManager.addBinaryOutputDataPoints(binaryOutputDataPoints.toArray(new String[0]));
		databaseManager.addAnalogInputDataPoints(analogInputDataPoints.toArray(new String[0]));
		databaseManager.addAnalogOutputDataPoints(analogOutputDataPoints.toArray(new String[0]));
		databaseManager.addCounterDataPoints(counterDataPoints.toArray(new String[0]));
		
		for (BinaryInputDataPoint dataPoint : databaseManager.getBinaryInputDataPoints()) {
			BinaryInputDataPoint point = this.cloneObject(templateBinaryInputDataPoint, BinaryInputDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setBinaryInputDataPoint(point);
		}
		for (DoubleBitBinaryInputDataPoint dataPoint : databaseManager.getDoubleBitBinaryInputDataPoints()) {
			DoubleBitBinaryInputDataPoint point = this.cloneObject(templateDoubleBitBinaryInputDataPoint, DoubleBitBinaryInputDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setDoubleBitBinaryInputDataPoint(point);
		}
		for (BinaryOutputDataPoint dataPoint : databaseManager.getBinaryOutputDataPoints()) {
			BinaryOutputDataPoint point = this.cloneObject(templateBinaryOutputDataPoint, BinaryOutputDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setBinaryOutputDataPoint(point);
		}
		for (AnalogInputDataPoint dataPoint : databaseManager.getAnalogInputDataPoints()) {
			AnalogInputDataPoint point = this.cloneObject(templateAnalogInputDataPoint, AnalogInputDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setAnalogInputDataPoint(point);
		}
		for (AnalogOutputDataPoint dataPoint : databaseManager.getAnalogOutputDataPoints()) {
			AnalogOutputDataPoint point = this.cloneObject(templateAnalogOutputDataPoint, AnalogOutputDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setAnalogOutputDataPoint(point);
		}
		for (CounterDataPoint dataPoint : databaseManager.getCounterDataPoints()) {
			CounterDataPoint point = this.cloneObject(templateCounterDataPoint, CounterDataPoint.class);
			point.setIndex(dataPoint.getIndex());
			point.setName(dataPoint.getName());
			databaseManager.setCounterDataPoint(point);
		}
		
		databaseManager.addBinaryInputDataPoints(extendedConfiguration.getBinaryInputPoints().toArray(new String[0]));
		databaseManager.addDoubleBitBinaryInputDataPoints(extendedConfiguration.getDoubleBitBinaryInputPoints().toArray(new String[0]));
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
		outstation.addRequestHandler(new DoubleBitBinaryInputStaticHandler(databaseManager));
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
		databaseManager.addEventListener(new DoubleBitBinaryInputEventListener(outstation));
		databaseManager.addEventListener(new BinaryOutputEventListener(outstation));
		databaseManager.addEventListener(new AnalogInputEventListener(outstation));
		databaseManager.addEventListener(new AnalogOutputEventListener(outstation));
		databaseManager.addEventListener(new CounterEventListener(outstation));
		
		outstation.setPrimaryAddress(primaryAddress);
		outstationDevice.setOutstation(outstation);
		outstationDevice.setDatabaseManager(databaseManager);
		DeviceProvider.registerDevice(outstationDevice);
		
		return outstationDevice;
	}

	public void setBinaryInputDataPoints(String... binaryInputDataPoints) {
		this.binaryInputDataPoints = asList(binaryInputDataPoints);
	}

	public void setDoubleBitBinaryInputDataPoints(String... doubleBitBinaryInputDataPoints) {
		this.doubleBitBinaryInputDataPoints = asList(doubleBitBinaryInputDataPoints);
	}

	public void setBinaryOutputDataPoints(String... binaryOutputDataPoints) {
		this.binaryOutputDataPoints = asList(binaryOutputDataPoints);
	}

	public void setAnalogInputDataPoints(String... analogInputDataPoints) {
		this.analogInputDataPoints = asList(analogInputDataPoints);
	}

	public void setAnalogOutputDataPoints(String... analogOutputDataPoints) {
		this.analogOutputDataPoints = asList(analogOutputDataPoints);
	}

	public void setCounterDataPoints(String... counterDataPoints) {
		this.counterDataPoints = asList(counterDataPoints);
	}

	public void setTemplateBinaryInputDataPoint(BinaryInputDataPoint templateBinaryInputDataPoint) {
		this.templateBinaryInputDataPoint = templateBinaryInputDataPoint;
	}

	public void setTemplateDoubleBitBinaryInputDataPoint(DoubleBitBinaryInputDataPoint templateDoubleBitBinaryInputDataPoint) {
		this.templateDoubleBitBinaryInputDataPoint = templateDoubleBitBinaryInputDataPoint;
	}

	public void setTemplateBinaryOutputDataPoint(BinaryOutputDataPoint templateBinaryOutputDataPoint) {
		this.templateBinaryOutputDataPoint = templateBinaryOutputDataPoint;
	}

	public void setTemplateAnalogInputDataPoint(AnalogInputDataPoint templateAnalogInputDataPoint) {
		this.templateAnalogInputDataPoint = templateAnalogInputDataPoint;
	}

	public void setTemplateAnalogOutputDataPoint(AnalogOutputDataPoint templateAnalogOutputDataPoint) {
		this.templateAnalogOutputDataPoint = templateAnalogOutputDataPoint;
	}

	public void setTemplateCounterDataPoint(CounterDataPoint templateCounterDataPoint) {
		this.templateCounterDataPoint = templateCounterDataPoint;
	}

	private <T> T cloneObject(T obj, Class<T> clazz) {
		try {
			return OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(obj), clazz);
		} catch(Exception e) {
			throw new RuntimeException(e);
		}
	}
}
