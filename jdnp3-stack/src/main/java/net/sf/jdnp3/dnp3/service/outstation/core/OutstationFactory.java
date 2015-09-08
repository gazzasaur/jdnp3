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
package net.sf.jdnp3.dnp3.service.outstation.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.service.outstation.adaptor.BinaryInputEventReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.BinaryInputStaticReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class0ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class1ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.CrobRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.BinaryInputStaticObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class0ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class1ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class2ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.Class3ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.CrobObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.InternalIndicatorBitObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDataDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationRequestHandler;

public class OutstationFactory {
	private List<ObjectTypeDecoder> decoders = new ArrayList<>();
	private List<OutstationRequestHandlerAdaptor> adaptors = new ArrayList<>();
	private Map<Class<? extends ObjectInstance>, ObjectType> mapping = new HashMap<>();
	private List<OutstationRequestHandler> outstationRequestHandlers = new ArrayList<>();
	private List<OutstationApplicationRequestHandler> outstationApplicationRequestHandlers = new ArrayList<>();
	
	public void addOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor adaptor) {
		adaptors.add(adaptor);
	}

	public void addOutstationRequestHandler(OutstationRequestHandler handler) {
		outstationRequestHandlers.add(handler);
	}

	public void addOutstationApplicationRequestHandler(OutstationApplicationRequestHandler handler) {
		outstationApplicationRequestHandlers.add(handler);
	}
	
	public void addObjectTypeDecoder(ObjectTypeDecoder objectTypeDecoder) {
		decoders.add(objectTypeDecoder);
	}
	
	public void addObjectTypeMapping(Class<? extends ObjectInstance> clazz, ObjectType objectType) {
		mapping.put(clazz, objectType);
	}
	
	public void addStandardOutstationRequestHandlerAdaptors() {
		adaptors.add(new BinaryInputStaticReadRequestAdaptor());
		adaptors.add(new BinaryInputEventReadRequestAdaptor());
		adaptors.add(new Class0ReadRequestAdaptor());
		adaptors.add(new Class1ReadRequestAdaptor());
		adaptors.add(new CrobRequestAdaptor());
	}
	
	public void addStandardObjectTypeDecoders() {
		decoders.add(new BinaryInputStaticObjectTypeDecoder());
		decoders.add(new Class0ObjectTypeDecoder());
		decoders.add(new Class1ObjectTypeDecoder());
		decoders.add(new Class2ObjectTypeDecoder());
		decoders.add(new Class3ObjectTypeDecoder());
		decoders.add(new CrobObjectTypeDecoder());
		decoders.add(new InternalIndicatorBitObjectTypeDecoder());
	}

	public Outstation createOutstation() {
		ObjectFragmentDataDecoder objectFragmentDataDecoder = new ObjectFragmentDataDecoder();
		for (ObjectTypeDecoder decoder : decoders) {
			objectFragmentDataDecoder.addObjectTypeDecoder(decoder);
		}
		decoders.clear();
		
		ObjectFragmentDecoder objectFragmentDecoder = new ObjectFragmentDecoder(objectFragmentDataDecoder);
		ApplicationFragmentRequestDecoder applicationFragmentRequestDecoder = new ApplicationFragmentRequestDecoderImpl(objectFragmentDecoder);
		OutstationApplicationLayer outstationApplicationLayer = new OutstationApplicationLayer();
		outstationApplicationLayer.setDecoder(applicationFragmentRequestDecoder);
		
		OutstationAdaptionLayer outstationAdaptionLayer = new OutstationAdaptionLayerImpl();
		outstationApplicationLayer.addRequestHandler(outstationAdaptionLayer);
		for (OutstationRequestHandlerAdaptor adaptor : adaptors) {
			outstationAdaptionLayer.addOutstationRequestHandlerAdaptor(adaptor);
		}
		adaptors.clear();

		OutstationImpl outstation = new OutstationImpl();
		outstation.setOutstationApplicationLayer(outstationApplicationLayer);
		outstation.setOutstationAdaptionLayer(outstationAdaptionLayer);
		
		for (OutstationApplicationRequestHandler outstationApplicationRequestHandler : outstationApplicationRequestHandlers) {
			outstationApplicationLayer.addRequestHandler(outstationApplicationRequestHandler);
		}
		outstationApplicationRequestHandlers.clear();
		
		for (OutstationRequestHandler outstationRequestHandler : outstationRequestHandlers) {
			outstation.addRequestHandler(outstationRequestHandler);
		}
		outstationRequestHandlers.clear();
		
		for (Class<? extends ObjectInstance> clazz : mapping.keySet()) {
			outstationApplicationLayer.addDefaultObjectTypeMapping(clazz, mapping.get(clazz));
		}
		
		return outstation;
	}
}