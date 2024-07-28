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
package net.sf.jdnp3.dnp3.service.outstation.core;

import static net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectTypeEncoderConstants.OBJECT_TYPE_ENCODERS;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_EVENT_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANALOG_OUTPUT_STATIC_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_STATIC_ANY;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_STATIC_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_EVENT_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_OUTPUT_STATIC_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_EVENT_GROUP;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.COUNTER_STATIC_GROUP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.jdnp3.dnp3.service.outstation.adaptor.AnalogOutputOperateRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.AssignClassRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.BinaryOutputOperateRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class0ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class1ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class2ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.Class3ReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.EventReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.InternalIndicatorWriteRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.StaticReadRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.adaptor.TimeAndDateRequestAdaptor;
import net.sf.jdnp3.dnp3.service.outstation.handler.generic.OutstationRequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.ItemEnumeratorFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.enumerator.StandardItemEnumeratorFactory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.analog.AnalogOutputCommandFloat32ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.analog.AnalogOutputCommandFloat64ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.analog.AnalogOutputCommandInteger16ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.analog.AnalogOutputCommandInteger32ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.binary.BinaryInputStaticFlagsObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.binary.CrobObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.binary.DoubleBitBinaryInputStaticFlagsObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ByteDataObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.Class0ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.Class1ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.Class2ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.Class3ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.InternalIndicatorBitObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.NoDataObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.generic.ObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.object.time.TimeAndDateObjectTypeDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDataDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ObjectFragmentDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.generic.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.CountRangeObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.CustomSingleObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.IndexRangeObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.SingleObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ObjectFragmentEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.analog.AnalogOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.BinaryOutputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ByteDataObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterEventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.counter.CounterStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.service.OutstationApplicationRequestHandler;

public class OutstationFactory {
	private InternalStatusProvider internalStatusProvider = null;
	private List<ObjectTypeEncoder> encoders = new ArrayList<>();
	private List<ObjectTypeDecoder> decoders = new ArrayList<>();
	private List<ObjectFragmentPacker> packers = new ArrayList<>();
	private List<ByteDataObjectTypeDecoder> customDecoders = new ArrayList<>();
	private List<OutstationRequestHandlerAdaptor> adaptors = new ArrayList<>();
	private List<ItemEnumeratorFactory> itemEnumeratorFactories = new ArrayList<>();
	private Map<Class<? extends ObjectInstance>, ObjectType> mapping = new HashMap<>();
	private List<OutstationRequestHandler> outstationRequestHandlers = new ArrayList<>();
	private List<OutstationApplicationRequestHandler> outstationApplicationRequestHandlers = new ArrayList<>();
	
	public void setInternalStatusProvider(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public void addObjectFragmentPacker(ObjectFragmentPacker packer) {
		packers.add(packer);
	}
	
	public void addOutstationRequestHandlerAdaptor(OutstationRequestHandlerAdaptor adaptor) {
		adaptors.add(adaptor);
	}

	public void addOutstationRequestHandler(OutstationRequestHandler handler) {
		outstationRequestHandlers.add(handler);
	}

	public void addOutstationApplicationRequestHandler(OutstationApplicationRequestHandler handler) {
		outstationApplicationRequestHandlers.add(handler);
	}
	
	public void addObjectTypeEncoder(ObjectTypeEncoder objectTypeEncoder) {
		encoders.add(objectTypeEncoder);
	}
	
	public void addObjectTypeDecoder(ObjectTypeDecoder objectTypeDecoder) {
		decoders.add(objectTypeDecoder);
	}

	public void addCustomDecoder(ByteDataObjectTypeDecoder byteDataObjectTypeDecoder) {
		customDecoders.add(byteDataObjectTypeDecoder);
	}
	
	public void addObjectTypeMapping(Class<? extends ObjectInstance> clazz, ObjectType objectType) {
		mapping.put(clazz, objectType);
	}
	
	public void addStandardObjectFragmentPackers() {
		packers.add(new CountRangeObjectFragmentPacker());
		packers.add(new IndexRangeObjectFragmentPacker());
		packers.add(new SingleObjectFragmentPacker());
		packers.add(new CustomSingleObjectFragmentPacker(ByteDataObjectInstance.class));
	}
	
	public void addStandardOutstationRequestHandlerAdaptors() {
		adaptors.add(new StaticReadRequestAdaptor<>(BINARY_INPUT_STATIC_GROUP, BinaryInputStaticObjectInstance.class));
		adaptors.add(new AssignClassRequestAdaptor<>(BINARY_INPUT_STATIC_GROUP, BinaryInputStaticObjectInstance.class));
		adaptors.add(new EventReadRequestAdaptor<>(BINARY_INPUT_EVENT_GROUP, BinaryInputEventObjectInstance.class));
		
		adaptors.add(new StaticReadRequestAdaptor<>(BINARY_OUTPUT_STATIC_GROUP, BinaryOutputStaticObjectInstance.class));
		adaptors.add(new EventReadRequestAdaptor<>(BINARY_OUTPUT_EVENT_GROUP, BinaryOutputEventObjectInstance.class));

		adaptors.add(new StaticReadRequestAdaptor<>(COUNTER_STATIC_GROUP, CounterStaticObjectInstance.class));
		adaptors.add(new EventReadRequestAdaptor<>(COUNTER_EVENT_GROUP, CounterEventObjectInstance.class));

		adaptors.add(new StaticReadRequestAdaptor<>(ANALOG_OUTPUT_STATIC_GROUP, AnalogOutputStaticObjectInstance.class));
		adaptors.add(new EventReadRequestAdaptor<>(ANALOG_OUTPUT_EVENT_GROUP, AnalogOutputEventObjectInstance.class));

		adaptors.add(new Class0ReadRequestAdaptor());
		adaptors.add(new Class1ReadRequestAdaptor());
		adaptors.add(new Class2ReadRequestAdaptor());
		adaptors.add(new Class3ReadRequestAdaptor());
		adaptors.add(new BinaryOutputOperateRequestAdaptor());
		adaptors.add(new AnalogOutputOperateRequestAdaptor());
		adaptors.add(new TimeAndDateRequestAdaptor());
		adaptors.add(new InternalIndicatorWriteRequestAdaptor());
	}
	
	public void addItemEnumeratorFactory(ItemEnumeratorFactory itemEnumeratorFactory) {
		itemEnumeratorFactories.add(itemEnumeratorFactory);
	}

	public void addStandardItemEnumeratorFactories() {
		itemEnumeratorFactories.add(new StandardItemEnumeratorFactory());
	}
	
	public void addStandardObjectTypeEncoders() {
		encoders.addAll(OBJECT_TYPE_ENCODERS);
	}
	
	public void addStandardObjectTypeDecoders() {
		decoders.add(new BinaryInputStaticFlagsObjectTypeDecoder());
		decoders.add(new DoubleBitBinaryInputStaticFlagsObjectTypeDecoder());
		decoders.add(new NoDataObjectTypeDecoder(BINARY_INPUT_STATIC_ANY));
		decoders.add(new Class0ObjectTypeDecoder());
		decoders.add(new Class1ObjectTypeDecoder());
		decoders.add(new Class2ObjectTypeDecoder());
		decoders.add(new Class3ObjectTypeDecoder());
		decoders.add(new AnalogOutputCommandInteger32ObjectTypeDecoder());
		decoders.add(new AnalogOutputCommandInteger16ObjectTypeDecoder());
		decoders.add(new AnalogOutputCommandFloat32ObjectTypeDecoder());
		decoders.add(new AnalogOutputCommandFloat64ObjectTypeDecoder());
		decoders.add(new CrobObjectTypeDecoder());
		decoders.add(new TimeAndDateObjectTypeDecoder());
		decoders.add(new InternalIndicatorBitObjectTypeDecoder());
	}

	public Outstation createOutstation() {
		ObjectFragmentEncoder objectFragmentEncoder = new ObjectFragmentEncoder();
		for (ObjectTypeEncoder encoder : encoders) {
			objectFragmentEncoder.addObjectTypeEncoder(encoder);
		}
		encoders.clear();
		
		ObjectFragmentDataDecoder objectFragmentDataDecoder = new ObjectFragmentDataDecoder();
		for (ObjectTypeDecoder decoder : decoders) {
			objectFragmentDataDecoder.addObjectTypeDecoder(decoder);
		}
		decoders.clear();
		
		for (ItemEnumeratorFactory itemEnumeratorFactory : itemEnumeratorFactories) {
			objectFragmentDataDecoder.addItemEnumeratorFactory(itemEnumeratorFactory);
		}
		itemEnumeratorFactories.clear();
		
		ObjectFragmentDecoder objectFragmentDecoder = new ObjectFragmentDecoder(objectFragmentDataDecoder);
		ApplicationFragmentResponseEncoder applicationFragmentResponseEncoder = new ApplicationFragmentResponseEncoderImpl(objectFragmentEncoder);
		ApplicationFragmentRequestDecoder applicationFragmentRequestDecoder = new ApplicationFragmentRequestDecoderImpl(objectFragmentDecoder);
		OutstationApplicationLayer outstationApplicationLayer = new OutstationApplicationLayer();
		outstationApplicationLayer.setEncoder(applicationFragmentResponseEncoder);
		outstationApplicationLayer.setDecoder(applicationFragmentRequestDecoder);
		
		for (ByteDataObjectTypeDecoder customDecoder : customDecoders) {
			objectFragmentDecoder.addCustomDecoder(customDecoder);
		}
		customDecoders.clear();
		
		if (internalStatusProvider != null) {
			outstationApplicationLayer.setInternalStatusProvider(internalStatusProvider);
		}
		internalStatusProvider = null;
		
		for (ObjectFragmentPacker packer : packers) {
			outstationApplicationLayer.addObjectFragmentPacker(packer);
		}
		packers.clear();
		
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
		
		for (Entry<Class<? extends ObjectInstance>, ObjectType> entry : mapping.entrySet()) {
			outstationApplicationLayer.addDefaultObjectTypeMapping(entry.getKey(), entry.getValue());
		}
		
		return outstation;
	}
}
