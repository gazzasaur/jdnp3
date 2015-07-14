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

import static java.lang.String.format;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.dnp3.service.outstation.handler.RequestHandler;
import net.sf.jdnp3.dnp3.stack.layer.application.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.Transaction;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoderDirectory;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.object.ObjectTypeEncoderDirectoryImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.sort.ObjectInstanceSorter;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.sort.ObjectInstanceTypeRationaliser;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponse;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponseHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayerImpl;

public class OutstationImpl implements ApplicationLayer {
	private List<OutstationServiceTypeHelper> outstationServiceTypeHelpers = new ArrayList<>();
	
	private DataLinkLayer dataLinkLayer = null;
	private Transaction currentTransaction = null;
	private TransportLayer transportLayer = new TransportLayerImpl();
	private ApplicationFragmentRequestDecoder decoder = new ApplicationFragmentRequestDecoderImpl();

	public DataLinkLayer getDataLinkLayer() {
		return dataLinkLayer;
	}

	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
		this.dataLinkLayer.setTransportLayer(transportLayer);
		this.transportLayer.setDataLinkLater(dataLinkLayer);
		this.transportLayer.setApplicationLayer(this);
	}
	
	public void addHandlerHelper(OutstationServiceTypeHelper outstationServiceTypeHelper) {
		outstationServiceTypeHelpers.add(outstationServiceTypeHelper);
	}
	
	public void addRequestHandler(RequestHandler requestHandler) {
		for (OutstationServiceTypeHelper outstationServiceTypeHelper : outstationServiceTypeHelpers) {
			if (outstationServiceTypeHelper.setHandler(requestHandler)) {
				break;
			}
		}
	}
	
	public void dataReceived(List<Byte> data) {
		// FIXME IMPL Need the ability to confirm a packet and complete/cancel a transaction.
		System.out.print("AL: ");
		for (Byte dataByte : data) {
			System.out.print(format("%02X ", dataByte));
		}
		System.out.println();
		
		List<ObjectInstance> responseObjects = new ArrayList<>();
		ApplicationFragmentRequest request = decoder.decode(data);
		for (ObjectFragment objectFragment : request.getObjectFragments()) {
			System.out.println(objectFragment.getObjectFragmentHeader().getObjectType());
			for (OutstationServiceTypeHelper helper : outstationServiceTypeHelpers) {
				System.out.println(helper);
				if (helper.canHandle(request.getHeader().getFunctionCode(), objectFragment)) {
					List<ObjectInstance> localResponse = new ArrayList<>();
					helper.doRequest(request.getHeader().getFunctionCode(), objectFragment, localResponse);
					responseObjects.addAll(localResponse);
					break;
				}
			}
		}
		
		ApplicationFragmentResponse response = new ApplicationFragmentResponse();
		ApplicationFragmentResponseHeader applicationResponseHeader = response.getHeader();
		applicationResponseHeader.setFunctionCode(FunctionCode.RESPONSE);
		applicationResponseHeader.getInternalIndicatorField().setDeviceRestart(false);
		applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
		applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
		applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
		
		ObjectInstanceTypeRationaliser rationaliser = new ObjectInstanceTypeRationaliser();
		for (ObjectInstance objectInstance : responseObjects) {
			rationaliser.rationaliseType(objectInstance);
		}
		ObjectInstanceSorter sorter = new ObjectInstanceSorter();
		sorter.sort(responseObjects);
		
		// FIXME IMPL Add the ability to add a Time Reference for relative time.
		// FIXME IMPL Add the ability to split fragment into multiple packets.
		long minIndex = 0;
		long maxIndex = 0;
		ObjectFragment objectFragment = null;
		ObjectInstance previousObjectInstance = null;
		ObjectTypeEncoderDirectory objectTypeEncoderDirectory = new ObjectTypeEncoderDirectoryImpl();
		for (ObjectInstance objectInstance : responseObjects) {
			ObjectTypeEncoder encoder = objectTypeEncoderDirectory.getObjectTypeEncoder(objectInstance);
			if (objectFragment == null) {
				minIndex = objectInstance.getIndex();
				maxIndex = objectInstance.getIndex();
				objectFragment = new ObjectFragment();
				objectFragment.getObjectFragmentHeader().setObjectType(objectInstance.getRequestedType());
				
				previousObjectInstance = null;
			} else if (previousObjectInstance != null && encoder.fragment(objectInstance, previousObjectInstance)) {
				Range range = encoder.calculateRangeType(objectFragment.getObjectFields().size(), minIndex, maxIndex, previousObjectInstance);
				ObjectPrefixCode objectPrefixCode = encoder.calculateObjectPrefix();
				
				objectFragment.getObjectFragmentHeader().getQualifierField().setObjectPrefixCode(objectPrefixCode);
				objectFragment.getObjectFragmentHeader().setRange(range);
				response.addObjectFragment(objectFragment);
				
				minIndex = objectInstance.getIndex();
				maxIndex = objectInstance.getIndex();
				objectFragment = new ObjectFragment();
				objectFragment.getObjectFragmentHeader().setObjectType(objectInstance.getRequestedType());
				previousObjectInstance = null;
			}
			if (objectInstance.getIndex() < minIndex) {
				minIndex = objectInstance.getIndex();
			}
			if (objectInstance.getIndex() > maxIndex) {
				maxIndex = objectInstance.getIndex();
			}
			
			Range range = encoder.calculateRangeType(objectFragment.getObjectFields().size(), minIndex, maxIndex, previousObjectInstance);
			ObjectPrefixCode objectPrefixCode = encoder.calculateObjectPrefix();
			objectFragment.getObjectFragmentHeader().getQualifierField().setObjectPrefixCode(objectPrefixCode);
			objectFragment.getObjectFragmentHeader().setRange(range);

			ObjectField objectField = new ObjectField();
			objectField.setObjectInstance(objectInstance);
			objectField.setPrefix(objectInstance.getIndex());
			objectFragment.addObjectField(objectField);
		}
		if (objectFragment != null && objectFragment.getObjectFields().size() > 0) {
			response.addObjectFragment(objectFragment);
		}
		
		transportLayer.sendData(new ApplicationFragmentResponseEncoderImpl().encode(response));
	}
}
