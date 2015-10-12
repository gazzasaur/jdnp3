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
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.database.AnalogInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.BinaryInputEventListener;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.DatabaseManagerProvider;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.AnalogInputStaticReader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.BinaryInputStaticReader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class0Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class1Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class2Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.Class3Reader;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.CrobOperator;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.InternalIndicatorWriter;
import net.sf.jdnp3.ui.web.outstation.message.dnp.handler.TimeAndDateHandler;

public class DeviceFactory {
	private List<String> binaryInputDataPoints = new ArrayList<>();
	private List<String> binaryOutputDataPoints = new ArrayList<>();
	private List<String> analogInputDataPoints = new ArrayList<>();
	
	public Outstation create(String siteName, String deviceName, DataLinkManager dataLink, int address) {
		DatabaseManager databaseManager = DatabaseManagerProvider.registerDevice(siteName, deviceName);
		databaseManager.addBinaryInputDataPoints(binaryInputDataPoints.toArray(new String[0]));
		databaseManager.addBinaryOutputDataPoints(binaryOutputDataPoints.toArray(new String[0]));
		databaseManager.addAnalogInputDataPoints(analogInputDataPoints.toArray(new String[0]));
		
		OutstationFactory outstationFactory = new OutstationFactory();
		outstationFactory.addStandardObjectTypeEncoders();
		outstationFactory.addStandardObjectTypeDecoders();
		outstationFactory.addStandardItemEnumeratorFactories();
		outstationFactory.addStandardOutstationRequestHandlerAdaptors();
		outstationFactory.setInternalStatusProvider(databaseManager.getInternalStatusProvider());
		
		Outstation outstation = outstationFactory.createOutstation();
		outstation.addRequestHandler(new BinaryInputStaticReader(databaseManager));
		outstation.addRequestHandler(new AnalogInputStaticReader(databaseManager));
		outstation.addRequestHandler(new Class0Reader(databaseManager));
		outstation.addRequestHandler(new Class1Reader());
		outstation.addRequestHandler(new Class2Reader());
		outstation.addRequestHandler(new Class3Reader());
		outstation.addRequestHandler(new CrobOperator(databaseManager));
		outstation.addRequestHandler(new TimeAndDateHandler(databaseManager.getInternalStatusProvider()));
		outstation.addRequestHandler(new InternalIndicatorWriter(databaseManager.getInternalStatusProvider()));
		
		databaseManager.addEventListener(new BinaryInputEventListener(outstation));
		databaseManager.addEventListener(new AnalogInputEventListener(outstation));
		
		outstation.setPrimaryAddress(address);
		dataLink.bind(3, outstation);
		
		return outstation;
	}

	public void setBinaryInputDataPoints(String... binaryInputDataPoints) {
		this.binaryInputDataPoints = asList(binaryInputDataPoints);
	}

	public void setBinaryOutputDataPoints(String... binaryOutputDataPoints) {
		this.binaryOutputDataPoints = asList(binaryOutputDataPoints);
	}

	public void setAnalogInputDataPoints(String... analogInputDataPoints) {
		this.analogInputDataPoints = asList(analogInputDataPoints);
	}
}
