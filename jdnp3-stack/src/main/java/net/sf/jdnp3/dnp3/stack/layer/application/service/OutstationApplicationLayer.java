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
package net.sf.jdnp3.dnp3.stack.layer.application.service;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_RELATIVE_TIME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util.DefaultObjectTypeMapping;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util.ObjectInstanceSorter;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.util.ObjectInstanceTypeRationaliser;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponse;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponseHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.CtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.SynchronisedCtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayerImpl;

public class OutstationApplicationLayer implements ApplicationLayer {
	private List<EventObjectInstance> pendingEvents = new ArrayList<>();
	private List<OutstationApplicationRequestHandler> outstationRequestHandlers = new ArrayList<>();
	
	private DataLinkLayer dataLinkLayer = null;
	private TransportLayer transportLayer = new TransportLayerImpl();
	private OutstationEventQueue eventQueue = new OutstationEventQueue();
	
	private ApplicationFragmentRequestDecoder decoder = null;
	
	private DefaultObjectTypeMapping defaultObjectTypeMapping = new DefaultObjectTypeMapping();

	public DataLinkLayer getDataLinkLayer() {
		return dataLinkLayer;
	}

	public void setDataLinkLayer(DataLinkLayer dataLinkLayer) {
		this.dataLinkLayer = dataLinkLayer;
		this.dataLinkLayer.addDataLinkLayerListener(transportLayer);
		this.transportLayer.setDataLinkLater(dataLinkLayer);
		this.transportLayer.setApplicationLayer(this);
	}
	
	public void addRequestHandler(OutstationApplicationRequestHandler outstationRequestHandler) {
		outstationRequestHandlers.add(outstationRequestHandler);
	}
	
	public void addDefaultObjectTypeMapping(Class<? extends ObjectInstance> clazz, ObjectType defaultMapping) {
		defaultObjectTypeMapping.addMapping(clazz, defaultMapping);
	}
	
	public OutstationEventQueue getOutstationEventQueue() {
		return eventQueue;
	}
	
	public void setDecoder(ApplicationFragmentRequestDecoder decoder) {
		this.decoder = decoder;
	}
	
	public void dataReceived(List<Byte> data) {
		List<ObjectInstance> responseObjects = new ArrayList<>();
		ApplicationFragmentRequest request = decoder.decode(data);

		if (request.getHeader().getFunctionCode() == FunctionCode.CONFIRM) {
			for (EventObjectInstance eventObjectInstance : pendingEvents) {
				eventQueue.confirm(eventObjectInstance);
			}
			pendingEvents.clear();
			return;
		} else {
			for (EventObjectInstance eventObjectInstance : pendingEvents) {
				eventQueue.cancelled(eventObjectInstance);
			}
			pendingEvents.clear();
		}
		
		for (ObjectFragment objectFragment : request.getObjectFragments()) {
			for (OutstationApplicationRequestHandler handler : outstationRequestHandlers) {
				if (handler.canHandle(request.getHeader().getFunctionCode(), objectFragment)) {
					List<ObjectInstance> localResponse = new ArrayList<>();
					handler.doRequest(request.getHeader().getFunctionCode(), eventQueue, objectFragment, localResponse);
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
		applicationResponseHeader.getApplicationControl().setConfirmationRequired(true);
		applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
		
		DefaultObjectTypeMapping mapping = new DefaultObjectTypeMapping();
		ObjectInstanceTypeRationaliser rationaliser = new ObjectInstanceTypeRationaliser();
		List<ObjectType> relativeTimeTypes = Arrays.asList(BINARY_INPUT_EVENT_RELATIVE_TIME);
		
		for (ObjectInstance objectInstance : responseObjects) {
			mapping.performMapping(objectInstance);
			rationaliser.rationaliseType(objectInstance);
		}
		ObjectInstanceSorter sorter = new ObjectInstanceSorter();
		sorter.sort(responseObjects);
		
		CtoObjectInstance ctoObjectInstance = null;
		for (ObjectInstance objectInstance : responseObjects) {
			if (objectInstance instanceof EventObjectInstance) {
				EventObjectInstance eventObjectInstance = (EventObjectInstance) objectInstance;
				if (relativeTimeTypes.contains(eventObjectInstance.getRequestedType()) && (ctoObjectInstance == null || Math.abs(ctoObjectInstance.getTimestamp() - eventObjectInstance.getTimestamp()) > 32767)) {
					// FIXME Possibly consider unsynchronised.
					SynchronisedCtoObjectInstance newCtoObjectInstance = new SynchronisedCtoObjectInstance();
					newCtoObjectInstance.setTimestamp(eventObjectInstance.getTimestamp());
					response.addObjectInstance(newCtoObjectInstance);
					ctoObjectInstance = newCtoObjectInstance;
				}
				
				pendingEvents.add((EventObjectInstance) objectInstance);
			}
			response.addObjectInstance(objectInstance);
		}
		
		transportLayer.sendData(new ApplicationFragmentResponseEncoderImpl().encode(response));
	}
}
