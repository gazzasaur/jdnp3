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
package net.sf.jdnp3.inttest.outstation.layer.application;

import static java.util.Arrays.asList;
import static javax.xml.bind.DatatypeConverter.parseHexBinary;
import static javax.xml.bind.DatatypeConverter.printHexBinary;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.dnp3.service.outstation.handler.BinaryInputStaticReadRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationTransport;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

@RunWith(JMockit.class)
public class ReadBinaryInputIntegrationTest {
	private Outstation outstation;
	private String transportData = "";
	private MessageProperties dummyMessageProperties;
	private List<BinaryInputStaticObjectInstance> dummyBinaryInputStaticObjectInstances;
	
	private ApplicationTransport mockApplicationTransport = new ApplicationTransport() {
		public void sendData(MessageProperties messageProperties, List<Byte> data) {
			transportData = printHexBinary(toPrimitive(data.toArray(new Byte[0])));
		}
	};
	private InternalStatusProvider mockInternalStatusProvider = new SimpleInternalStatusProvider();
	@Injectable private BinaryInputStaticReadRequestHandler mockBinaryInputStaticReadRequestHandler;
	
	@Before
	public void setup() {
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		setupOutstation();
	}

	private void setupOutstation() {
		transportData = "";
		buildOutstation();
		dummyMessageProperties = new MessageProperties();
		dummyMessageProperties.setMaster(true);
		
		dummyBinaryInputStaticObjectInstances = new ArrayList<>();
		dummyBinaryInputStaticObjectInstances.add(new BinaryInputStaticObjectInstance());
		dummyBinaryInputStaticObjectInstances.add(new BinaryInputStaticObjectInstance());
		dummyBinaryInputStaticObjectInstances.get(1).setIndex(255);
		dummyBinaryInputStaticObjectInstances.get(1).setActive(true);
		dummyBinaryInputStaticObjectInstances.get(1).setOnline(false);
		dummyBinaryInputStaticObjectInstances.add(new BinaryInputStaticObjectInstance());
		dummyBinaryInputStaticObjectInstances.get(2).setIndex(256);
		dummyBinaryInputStaticObjectInstances.get(2).setActive(true);
		dummyBinaryInputStaticObjectInstances.get(2).setOnline(false);
	}
	
	@Test
	public void readAllDefault() {
		new NonStrictExpectations() {{
			mockBinaryInputStaticReadRequestHandler.getObjectInstanceClass(); result = BinaryInputStaticObjectInstance.class;
			mockBinaryInputStaticReadRequestHandler.readStatics(); result = dummyBinaryInputStaticObjectInstances;
		}};
		List<Byte> data = asList(toObject(parseHexBinary("C701010006")));
		outstation.addRequestHandler(mockBinaryInputStaticReadRequestHandler);
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, data);
		assertThat(transportData, is("C7810000010100000000010201FF0000018080"));
	}
	
	@Test
	public void readIndexRangeDefault() {
		new NonStrictExpectations() {{
			mockBinaryInputStaticReadRequestHandler.getObjectInstanceClass(); result = BinaryInputStaticObjectInstance.class;
			mockBinaryInputStaticReadRequestHandler.readStatics(0, 2); result = dummyBinaryInputStaticObjectInstances;
		}};
		List<Byte> dataShort = asList(toObject(parseHexBinary("C7010100000002")));
		List<Byte> dataLong = asList(toObject(parseHexBinary("C70101000100000200")));
		List<Byte> dataExtraLong = asList(toObject(parseHexBinary("C7010100020000000002000000")));
		outstation.addRequestHandler(mockBinaryInputStaticReadRequestHandler);
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, dataShort);
		assertThat(transportData, is("C7810000010100000000010201FF0000018080"));
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, dataLong);
		assertThat(transportData, is("C7810000010100000000010201FF0000018080"));
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, dataExtraLong);
		assertThat(transportData, is("C7810000010100000000010201FF0000018080"));
	}

	@Test
	public void readIndividualIndexDefault() {
		new NonStrictExpectations() {{
			mockBinaryInputStaticReadRequestHandler.getObjectInstanceClass(); result = BinaryInputStaticObjectInstance.class;
			mockBinaryInputStaticReadRequestHandler.readStatic(0); result = dummyBinaryInputStaticObjectInstances.get(0);
			mockBinaryInputStaticReadRequestHandler.readStatic(1); result = dummyBinaryInputStaticObjectInstances.get(1);
			mockBinaryInputStaticReadRequestHandler.readStatic(2); result = dummyBinaryInputStaticObjectInstances.get(2);
		}};
		List<Byte> data = asList(toObject(parseHexBinary("C70101002801000100")));
		outstation.addRequestHandler(mockBinaryInputStaticReadRequestHandler);
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, data);
		assertThat(transportData, is("C7810000010200FFFF80"));
	}

	private void buildOutstation() {
		OutstationFactory factory = new OutstationFactory();
		factory.setInternalStatusProvider(mockInternalStatusProvider);
		factory.addStandardOutstationRequestHandlerAdaptors();
		factory.addStandardItemEnumeratorFactories();
		factory.addStandardObjectFragmentPackers();
		factory.addStandardObjectTypeDecoders();
		factory.addStandardObjectTypeEncoders();
		outstation = factory.createOutstation();
		outstation.setApplicationTransport(mockApplicationTransport);
	}
}
