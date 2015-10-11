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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.CONFIRM;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DELAY_MEASURE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.RESPONSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants.BINARY_INPUT_EVENT_RELATIVE_TIME;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.exception.UnknownObjectException;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.CountRangeObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.IndexRangeObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPackerContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPackerResult;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.SingleObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoder;
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
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.StaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.SynchronisedCtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.TimeDelayObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OutstationApplicationLayer implements ApplicationLayer {
	private Logger logger = LoggerFactory.getLogger(OutstationApplicationLayer.class);
	
	private int mtu = 2048;
	private List<EventObjectInstance> pendingEvents = new ArrayList<>();
	private List<OutstationApplicationRequestHandler> outstationRequestHandlers = new ArrayList<>();
	
	private DataLinkLayer dataLinkLayer = null;
	private InternalStatusProvider internalStatusProvider = null;
	private OutstationEventQueue eventQueue = new OutstationEventQueue();
	
	private ApplicationTransport applicationTransport;
	private ApplicationFragmentRequestDecoder decoder = null;
	private ApplicationFragmentResponseEncoder encoder = null;
	private DefaultObjectTypeMapping defaultObjectTypeMapping = new DefaultObjectTypeMapping();

	public DataLinkLayer getDataLinkLayer() {
		return dataLinkLayer;
	}

	public void setApplicationTransport(ApplicationTransport applicationTransport) {
		this.applicationTransport = applicationTransport;
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
	
	public void setEncoder(ApplicationFragmentResponseEncoder encoder) {
		this.encoder = encoder;
	}
	
	public void setDecoder(ApplicationFragmentRequestDecoder decoder) {
		this.decoder = decoder;
	}
	
	public void setInternalStatusProvider(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
		eventQueue.setInternalStatusProvider(internalStatusProvider);
	}
	
	public void dataReceived(MessageProperties messageProperties, List<Byte> data) {
		if (applicationTransport == null) {
			throw new IllegalStateException("No ApplicationTransport has been defined.");
		}
		if (encoder == null) {
			throw new IllegalStateException("An encoder must be specified.");
		}
		if (decoder == null) {
			throw new IllegalStateException("A decoder must be specified.");
		}
		data = new ArrayList<>(data);
		
		// FIXME IMPL Will also have to account for Broadcast.
		if (!messageProperties.isMaster()) {
			return;
		}
		MessageProperties returnMessageProperties = new MessageProperties();
		returnMessageProperties.setSourceAddress(messageProperties.getDestinationAddress());
		returnMessageProperties.setDestinationAddress(messageProperties.getSourceAddress());
		returnMessageProperties.setTimeReceived(messageProperties.getTimeReceived());
		returnMessageProperties.setChannelId(messageProperties.getChannelId());
		
		List<ObjectInstance> responseObjects = new ArrayList<>();
		
		ApplicationFragmentRequest request = new ApplicationFragmentRequest();
		try {
			decoder.decode(request, data);
		} catch (UnknownObjectException unknownObjectException) {
			logger.error(unknownObjectException.getMessage(), unknownObjectException);
			if (request.getHeader().getApplicationControl().getSequenceNumber() >= 0) {
				ApplicationFragmentResponse response = new ApplicationFragmentResponse();
				ApplicationFragmentResponseHeader applicationResponseHeader = response.getHeader();
				applicationResponseHeader.setFunctionCode(RESPONSE);
				applicationResponseHeader.getInternalIndicatorField().setObjectUnknown(true);
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
				applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
				applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
				applicationTransport.sendData(returnMessageProperties, encoder.encode(response));
			}
			return;
		}

		if (request.getHeader().getFunctionCode() == CONFIRM) {
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
		
		if (request.getHeader().getFunctionCode() == DELAY_MEASURE) {
			TimeDelayObjectInstance timeDelayObjectInstance = new TimeDelayObjectInstance();
			timeDelayObjectInstance.setIndex(1);
			timeDelayObjectInstance.setTimestamp(2*(new Date().getTime() - returnMessageProperties.getTimeReceived()));
			responseObjects.add(new TimeDelayObjectInstance());
		}

		// FIXME IMPL Raise DNP3 error if it cannot be handled.
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
		applicationResponseHeader.setFunctionCode(RESPONSE);
		if (internalStatusProvider != null) {
			try {
				BeanUtils.copyProperties(applicationResponseHeader.getInternalIndicatorField(), internalStatusProvider);
			} catch (Exception e) {
				applicationResponseHeader.getInternalIndicatorField().setDeviceTrouble(true);
				logger.error("Cannot copy IIN properties.", e);
			}
		}
		applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
		applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
		applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
		
		if (request.getHeader().getFunctionCode().equals(DIRECT_OPERATE)) {
			for (ObjectFragment objectFragment : request.getObjectFragments()) {
				response.addObjectFragment(objectFragment);
			}
			applicationTransport.sendData(returnMessageProperties, encoder.encode(response));
			return;
		}
		
		DefaultObjectTypeMapping mapping = new DefaultObjectTypeMapping();
		ObjectInstanceTypeRationaliser rationaliser = new ObjectInstanceTypeRationaliser();
		List<ObjectType> relativeTimeTypes = Arrays.asList(BINARY_INPUT_EVENT_RELATIVE_TIME);
		
		for (ObjectInstance objectInstance : responseObjects) {
			mapping.performMapping(objectInstance);
			rationaliser.rationaliseType(objectInstance);
		}
		ObjectInstanceSorter sorter = new ObjectInstanceSorter();
		sorter.sort(responseObjects);
		
		List<ObjectInstance> replyObjects = new ArrayList<>();
		
		CtoObjectInstance ctoObjectInstance = null;
		for (ObjectInstance objectInstance : responseObjects) {
			if (objectInstance instanceof EventObjectInstance) {
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(true);
				EventObjectInstance eventObjectInstance = (EventObjectInstance) objectInstance;
				if (relativeTimeTypes.contains(eventObjectInstance.getRequestedType()) && (ctoObjectInstance == null || Math.abs(ctoObjectInstance.getTimestamp() - eventObjectInstance.getTimestamp()) > 32767)) {
					// FIXME Possibly consider unsynchronised.
					SynchronisedCtoObjectInstance newCtoObjectInstance = new SynchronisedCtoObjectInstance();
					newCtoObjectInstance.setTimestamp(eventObjectInstance.getTimestamp());
					replyObjects.add(newCtoObjectInstance);
					ctoObjectInstance = newCtoObjectInstance;
				}
				
				pendingEvents.add((EventObjectInstance) objectInstance);
			}
			replyObjects.add(objectInstance);
		}
		
		ObjectFragmentPacker singlePacker = new SingleObjectFragmentPacker();
		ObjectFragmentPacker indexPacker = new IndexRangeObjectFragmentPacker();
		ObjectFragmentPacker countPacker = new CountRangeObjectFragmentPacker();
		
		ObjectFragmentPackerContext context = new ObjectFragmentPackerContext();
		context.setFreeSpace(2048);
		context.setTimeReference(0);
		context.setFunctionCode(FunctionCode.RESPONSE);
		while (replyObjects.size() > 0) {
			if (replyObjects.get(0) instanceof CtoObjectInstance) {
				context.setTimeReference(((CtoObjectInstance)replyObjects.get(0)).getTimestamp());
			}
			
			ObjectFragmentPacker packer = countPacker;
			if (replyObjects.get(0) instanceof StaticObjectInstance) {
				packer = indexPacker;
			} else if (replyObjects.get(0) instanceof CtoObjectInstance) {
				packer = singlePacker;
			} else if (replyObjects.get(0) instanceof TimeDelayObjectInstance) {
				packer = singlePacker;
			}
			
			ObjectFragmentPackerResult result = packer.pack(context, replyObjects);
			response.addObjectFragment(result.getObjectFragment());
		}
		
		applicationTransport.sendData(returnMessageProperties, encoder.encode(response));
	}

	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}

}
