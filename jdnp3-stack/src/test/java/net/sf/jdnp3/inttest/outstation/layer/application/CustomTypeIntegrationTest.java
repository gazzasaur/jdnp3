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
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.WRITE;
import static org.apache.commons.lang3.ArrayUtils.toObject;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;

import mockit.NonStrictExpectations;
import mockit.integration.junit4.JMockit;
import net.sf.jdnp3.dnp3.service.outstation.core.ByteDataOutstationApplicationRequestHandler;
import net.sf.jdnp3.dnp3.service.outstation.core.Outstation;
import net.sf.jdnp3.dnp3.service.outstation.core.OutstationFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ByteDataObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.CustomSingleObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationTransport;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;

@RunWith(JMockit.class)
public class CustomTypeIntegrationTest {
	private Outstation outstation;
	private String transportData = "";
	private MessageProperties dummyMessageProperties;
	
	private ApplicationTransport mockApplicationTransport = new ApplicationTransport() {
		public void sendData(MessageProperties messageProperties, List<Byte> data) {
			transportData = printHexBinary(toPrimitive(data.toArray(new Byte[0])));
		}
	};
	private InternalStatusProvider mockInternalStatusProvider = new SimpleInternalStatusProvider();
	
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
	}
	
	@Test
	public void readAllDefault() {
		new NonStrictExpectations() {{
		}};
		List<Byte> data = asList(toObject(parseHexBinary("C30246011B01290700000000007F0080000000000000000000000000000000000000000000000009004944")));
		outstation.getApplicationLayer().dataReceived(dummyMessageProperties, data);
		assertThat(transportData, is("C381000046011B01290700000000007F00800000000000000009005065"));
	}
	
	private void buildOutstation() {
		OutstationFactory factory = new OutstationFactory();
		factory.addOutstationApplicationRequestHandler(new ByteDataOutstationApplicationRequestHandler());
		factory.addCustomDecoder(new ByteDataObjectTypeDecoder(WRITE, "46011B01290700000000007F0080000000000000000000000000000000000000000000000009004944", "46011B01290700000000007F00800000000000000009005065"));
		factory.addObjectFragmentPacker(new CustomSingleObjectFragmentPacker(ByteDataObjectInstance.class));
		factory.setInternalStatusProvider(mockInternalStatusProvider);
		factory.addStandardOutstationRequestHandlerAdaptors();
		factory.addStandardItemEnumeratorFactories();
		factory.addStandardObjectFragmentPackers();
		factory.addStandardObjectTypeDecoders();
		factory.addStandardObjectTypeEncoders();

		outstation = factory.createOutstation();
		outstation.addApplicationTransport(mockApplicationTransport);
	}
}
