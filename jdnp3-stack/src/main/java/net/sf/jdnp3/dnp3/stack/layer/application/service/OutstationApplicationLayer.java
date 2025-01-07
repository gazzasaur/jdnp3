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
package net.sf.jdnp3.dnp3.stack.layer.application.service;

import static java.util.Collections.unmodifiableList;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.CONFIRM;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DELAY_MEASURE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.RESPONSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.SELECT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.UNSOLICITED_RESPONSE;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.BINARY_INPUT_EVENT_RELATIVE_TIME;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_1;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_2;
import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.CLASS_3;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.exception.UnknownObjectException;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentDecoderContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoder;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPacker;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPackerContext;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packer.ObjectFragmentPackerResult;
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
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.EventObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.CtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.SynchronisedCtoObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.time.TimeDelayObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

public class OutstationApplicationLayer implements ApplicationLayer {
	private static final Logger LOGGER = LoggerFactory.getLogger(OutstationApplicationLayer.class);
	private static final List<ObjectType> RELATIVE_TIME_TYPES = Arrays.asList(BINARY_INPUT_EVENT_RELATIVE_TIME);
	
	private int mtu = 2048;
	private int address = 2;
	private List<EventObjectInstance> pendingEvents = new ArrayList<>();
	private List<OutstationApplicationRequestHandler> outstationRequestHandlers = new ArrayList<>();
	
	private DataLinkLayer dataLinkLayer = null;
	private InternalStatusProvider internalStatusProvider = null;
	private OutstationEventQueue eventQueue = new OutstationEventQueue();
	
	private ApplicationTransport applicationTransport;
	private ApplicationFragmentRequestDecoder decoder = null;
	private ApplicationFragmentResponseEncoder encoder = null;
	private List<ObjectFragmentPacker> packers = new ArrayList<>();
	private DefaultObjectTypeMapping defaultObjectTypeMapping = new DefaultObjectTypeMapping();

	private int expectedConfirmationSeqNr = -1;
	private LinkedList<ObjectInstance> remainingObjects = new LinkedList<ObjectInstance>();

	private Object unsolicitedLock = new Object();
	private int nextUnsolicitedSeqNr = 0;
	private int expectedUnsolicitedSeqNr = -1;
	private volatile MessageProperties unsolicitedMessageProperties = null;
	private List<EventObjectInstance> pendingUnsolicitedEvents = new ArrayList<>();

	private static final ScheduledExecutorService UNSOLICITED_THREAD_POOL = Executors.newSingleThreadScheduledExecutor();

	public OutstationApplicationLayer() {
		UNSOLICITED_THREAD_POOL.scheduleWithFixedDelay(() -> this.sendUnsolicited(), 5000 + RandomUtils.nextInt(0, 1000), 5000 + RandomUtils.nextInt(0, 1000), TimeUnit.MILLISECONDS);
	}
	
	public DataLinkLayer getDataLinkLayer() {
		return dataLinkLayer;
	}

	public void addApplicationTransport(ApplicationTransport applicationTransport) {
		this.applicationTransport = applicationTransport;
	}
	
	public void removeApplicationTransport(ApplicationTransport applicationTransport) {
		this.applicationTransport = null;
	}
	
	public void addRequestHandler(OutstationApplicationRequestHandler outstationRequestHandler) {
		outstationRequestHandlers.add(outstationRequestHandler);
	}
	
	public void addDefaultObjectTypeMapping(Class<? extends ObjectInstance> clazz, ObjectType defaultMapping) {
		defaultObjectTypeMapping.addMapping(clazz, defaultMapping);
	}
	
	public void addObjectFragmentPacker(ObjectFragmentPacker packer) {
		packers.add(packer);
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

	public void sendUnsolicited() {
		synchronized (unsolicitedLock) {
			try {
				trySendUnsolicited();
			} catch (Exception e) {
				LOGGER.error("Failed to send unsolicited: ", e);
	
				expectedUnsolicitedSeqNr = -1;
				expectedConfirmationSeqNr = -1;
				for (EventObjectInstance event : pendingUnsolicitedEvents) {
					eventQueue.cancelled(event);
				}
				pendingUnsolicitedEvents.clear();
				nextUnsolicitedSeqNr += 1;
				nextUnsolicitedSeqNr %= 16;
	
				unsolicitedMessageProperties = null;
			}
		}
	}

	private void trySendUnsolicited() {
		MessageProperties returnMessageProperties = unsolicitedMessageProperties;

		if (!internalStatusProvider.isUnsolicitedEnabled() || returnMessageProperties == null) {
			expectedUnsolicitedSeqNr = -1;
			for (EventObjectInstance event : pendingUnsolicitedEvents) {
				eventQueue.cancelled(event);
			}
			pendingUnsolicitedEvents.clear();
			return;
		}

		// Timeout
		if (expectedUnsolicitedSeqNr >= 0) {
			expectedUnsolicitedSeqNr = -1;
			expectedConfirmationSeqNr = -1;
			for (EventObjectInstance event : pendingUnsolicitedEvents) {
				eventQueue.cancelled(event);
			}
			pendingUnsolicitedEvents.clear();
			nextUnsolicitedSeqNr += 1;
			nextUnsolicitedSeqNr %= 16;
			return;
		}

		List<ObjectInstance> responseObjects = new ArrayList<>();
		List<ObjectFragment> objectFragments = Arrays.asList(new ObjectFragment() {{
			getObjectFragmentHeader().setObjectType(CLASS_1);
		}}, new ObjectFragment() {{
			getObjectFragmentHeader().setObjectType(CLASS_2);
		}}, new ObjectFragment() {{
			getObjectFragmentHeader().setObjectType(CLASS_3);
		}});

		for (ObjectFragment objectFragment : objectFragments) {
			for (OutstationApplicationRequestHandler handler : outstationRequestHandlers) {
				if (handler.canHandle(FunctionCode.READ, objectFragment)) {
					List<ObjectInstance> localResponse = new ArrayList<>();
					handler.doRequest(FunctionCode.READ, eventQueue, objectFragment, localResponse);
					responseObjects.addAll(localResponse);
					break;
				}
			}
		}

		if (responseObjects.size() == 0) {
			return;
		};
		expectedUnsolicitedSeqNr = nextUnsolicitedSeqNr;

		LinkedList<ObjectInstance> replyObjects = new LinkedList<ObjectInstance>();

		ApplicationFragmentResponse response = new ApplicationFragmentResponse();
		ApplicationFragmentResponseHeader applicationResponseHeader = response.getHeader();
		applicationResponseHeader.setFunctionCode(UNSOLICITED_RESPONSE);
		applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
		applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(true);
		applicationResponseHeader.getApplicationControl().setSequenceNumber(nextUnsolicitedSeqNr);
		
		CtoObjectInstance ctoObjectInstance = null;
		for (ObjectInstance objectInstance : responseObjects) {
			if (objectInstance instanceof EventObjectInstance) {
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(true);
				EventObjectInstance eventObjectInstance = (EventObjectInstance) objectInstance;
				if (RELATIVE_TIME_TYPES.contains(eventObjectInstance.getRequestedType()) && (ctoObjectInstance == null || Math.abs(ctoObjectInstance.getTimestamp() - eventObjectInstance.getTimestamp()) > 32767)) {
					// FIXME Possibly consider unsynchronised.
					SynchronisedCtoObjectInstance newCtoObjectInstance = new SynchronisedCtoObjectInstance();
					newCtoObjectInstance.setTimestamp(eventObjectInstance.getTimestamp());
					replyObjects.add(newCtoObjectInstance);
					ctoObjectInstance = newCtoObjectInstance;
				}
				
				pendingUnsolicitedEvents.add((EventObjectInstance) objectInstance);
			}
			replyObjects.add(objectInstance);
		}
		
		ObjectFragmentPackerContext context = new ObjectFragmentPackerContext();
		context.setFreeSpace(2048 - 32); // Give some room to breath
		context.setTimeReference(0);
		context.setFunctionCode(FunctionCode.UNSOLICITED_RESPONSE);
		CtoObjectInstance previousTimeReference = null;

		while (replyObjects.size() > 0) {
			if (replyObjects.get(0) instanceof CtoObjectInstance) {
				previousTimeReference = (CtoObjectInstance)replyObjects.get(0);
				context.setTimeReference(previousTimeReference.getTimestamp());
			}
			
			ObjectFragmentPacker packer = null;
			// TODO Need to consider free space for all packers.
			for (ObjectFragmentPacker objectFragmentPacker : packers) {
				if (objectFragmentPacker.canPack(replyObjects.get(0).getClass())){
					packer = objectFragmentPacker;
				}
			}
			if (packer == null) {
				throw new IllegalStateException("No packer found for type: " + replyObjects.get(0).getClass());
			}
			
			ObjectFragmentPackerResult result = packer.pack(context, replyObjects);
			if (result.hasObjectFragment()) {
				response.addObjectFragment(result.getObjectFragment());
			}
			if (result.isAtCapacity()) {
				// An unsolicited response is not allowed to be freagmented. So just send it as it.
				break;
			}
		}

		applicationTransport.sendData(returnMessageProperties, encoder.encode(response));
	}

	public synchronized void dataReceived(MessageProperties messageProperties, List<Byte> data) {
		validateState();
		
		List<Byte> localData = new ArrayList<>(data);
		
		if (!messageProperties.isMaster()) {
			return;
		}
		MessageProperties returnMessageProperties = new MessageProperties();
		boolean broadcast = calculateReturnAddress(messageProperties, returnMessageProperties);
		if (!broadcast) {
			MessageProperties updateUnsolicitedMessageProperties = new MessageProperties();
			calculateReturnAddress(messageProperties, updateUnsolicitedMessageProperties);
			unsolicitedMessageProperties = updateUnsolicitedMessageProperties;
		}
		
		List<ObjectInstance> responseObjects = new ArrayList<>();
		
		ApplicationFragmentRequest request = new ApplicationFragmentRequest();
		ApplicationFragmentDecoderContext decoderContext = new ApplicationFragmentDecoderContext();
		try {
			decoder.decode(decoderContext, request, localData);
		} catch (UnknownObjectException unknownObjectException) {
			LOGGER.error("Failed to decode: " + DataUtils.toString(data));
			LOGGER.error(decoderContext.getDecodeLogic());
			LOGGER.error(unknownObjectException.getMessage(), unknownObjectException);
			if (request.getHeader().getApplicationControl().getSequenceNumber() >= 0) {
				ApplicationFragmentResponse response = new ApplicationFragmentResponse();
				response.getHeader().getInternalIndicatorField().setObjectUnknown(true);
				ApplicationFragmentResponseHeader applicationResponseHeader = response.getHeader();
				applicationResponseHeader.setFunctionCode(RESPONSE);
				applicationResponseHeader.getInternalIndicatorField().setObjectUnknown(true);
				applicationResponseHeader.getInternalIndicatorField().setBroadcast(broadcast);
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
				applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
				applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
				trySendData(returnMessageProperties, response);
			}
			return;
		}

		synchronized (unsolicitedLock) {
			if (request.getHeader().getFunctionCode() == CONFIRM
				&& request.getHeader().getApplicationControl().isUnsolicitedResponse()
				&& request.getHeader().getApplicationControl().getSequenceNumber() == expectedUnsolicitedSeqNr) {
					for (EventObjectInstance eventObjectInstance : pendingUnsolicitedEvents) {
						eventQueue.confirm(eventObjectInstance);
					}
					pendingUnsolicitedEvents.clear();
					expectedUnsolicitedSeqNr = -1;
					nextUnsolicitedSeqNr += 1;
					return;
			}
		}

		boolean firstFragment = true;
		if (request.getHeader().getFunctionCode() == CONFIRM
				&& !remainingObjects.isEmpty()
				&& request.getHeader().getApplicationControl().getSequenceNumber() == expectedConfirmationSeqNr) {
			responseObjects.addAll(remainingObjects);
			remainingObjects.clear();
			firstFragment = false;
		} else if (request.getHeader().getFunctionCode() == CONFIRM) { // FIXME This should check the sequence number
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
			remainingObjects.clear();
			expectedConfirmationSeqNr = -1;
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
		applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
		applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(firstFragment);
		applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
		applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
		applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
		
		// TODO Currently does not perform state checks
		if (request.getHeader().getFunctionCode().equals(DIRECT_OPERATE) || request.getHeader().getFunctionCode().equals(SELECT) || request.getHeader().getFunctionCode().equals(OPERATE)) {
			for (ObjectFragment objectFragment : request.getObjectFragments()) {
				response.addObjectFragment(objectFragment);
			}

			if (internalStatusProvider != null) {
				try {
					PropertyUtils.copyProperties(applicationResponseHeader.getInternalIndicatorField(), internalStatusProvider);
				} catch (Exception e) {
					applicationResponseHeader.getInternalIndicatorField().setDeviceTrouble(true);
					LOGGER.error("Cannot copy IIN properties.", e);
				}
			}
			applicationResponseHeader.getInternalIndicatorField().setBroadcast(applicationResponseHeader.getInternalIndicatorField().isBroadcast() || broadcast);

			sendData(messageProperties, localData, returnMessageProperties, response);
			return;
		}
		if (internalStatusProvider != null) {
			try {
				PropertyUtils.copyProperties(applicationResponseHeader.getInternalIndicatorField(), internalStatusProvider);
			} catch (Exception e) {
				applicationResponseHeader.getInternalIndicatorField().setDeviceTrouble(true);
				LOGGER.error("Cannot copy IIN properties.", e);
			}
		}
		applicationResponseHeader.getInternalIndicatorField().setBroadcast(applicationResponseHeader.getInternalIndicatorField().isBroadcast() || broadcast);
		
		DefaultObjectTypeMapping mapping = new DefaultObjectTypeMapping();
		ObjectInstanceTypeRationaliser rationaliser = new ObjectInstanceTypeRationaliser();
		
		for (ObjectInstance objectInstance : responseObjects) {
			mapping.performMapping(objectInstance);
			rationaliser.rationaliseType(objectInstance);
		}
		ObjectInstanceSorter sorter = new ObjectInstanceSorter();
		sorter.sort(responseObjects);
		
		LinkedList<ObjectInstance> replyObjects = new LinkedList<ObjectInstance>();
		
		CtoObjectInstance ctoObjectInstance = null;
		for (ObjectInstance objectInstance : responseObjects) {
			if (objectInstance instanceof EventObjectInstance) {
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(true);
				EventObjectInstance eventObjectInstance = (EventObjectInstance) objectInstance;
				if (RELATIVE_TIME_TYPES.contains(eventObjectInstance.getRequestedType()) && (ctoObjectInstance == null || Math.abs(ctoObjectInstance.getTimestamp() - eventObjectInstance.getTimestamp()) > 32767)) {
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
		
		ObjectFragmentPackerContext context = new ObjectFragmentPackerContext();
		context.setFreeSpace(2048 - 32); // Give some room to breath
		context.setTimeReference(0);
		context.setFunctionCode(FunctionCode.RESPONSE);
		CtoObjectInstance previousTimeReference = null;

		while (replyObjects.size() > 0) {
			if (replyObjects.get(0) instanceof CtoObjectInstance) {
				previousTimeReference = (CtoObjectInstance)replyObjects.get(0);
				context.setTimeReference(previousTimeReference.getTimestamp());
			}
			
			ObjectFragmentPacker packer = null;
			// TODO Need to consider free space for all packers.
			for (ObjectFragmentPacker objectFragmentPacker : packers) {
				if (objectFragmentPacker.canPack(replyObjects.get(0).getClass())){
					packer = objectFragmentPacker;
				}
			}
			if (packer == null) {
				throw new IllegalStateException("No packer found for type: " + replyObjects.get(0).getClass());
			}
			
			ObjectFragmentPackerResult result = packer.pack(context, replyObjects);
			if (result.hasObjectFragment()) {
				response.addObjectFragment(result.getObjectFragment());
			}
			if (result.isAtCapacity()) {
				response.getHeader().getApplicationControl().setFinalFragmentOfMessage(false);
				response.getHeader().getApplicationControl().setConfirmationRequired(true);
				expectedConfirmationSeqNr = response.getHeader().getApplicationControl().getSequenceNumber();
				remainingObjects = replyObjects;
				if (previousTimeReference != null) {
					remainingObjects.addFirst(previousTimeReference);
				}
				break;
			}
		}
		
		trySendData(returnMessageProperties, response);
	}

	private void trySendData(MessageProperties returnMessageProperties, ApplicationFragmentResponse response) {
		try {
			applicationTransport.sendData(returnMessageProperties, encoder.encode(response));
		} catch (Exception e) {
			LOGGER.error("Failed to send message to DNP3 address {} on channel {}.", returnMessageProperties.getDestinationAddress(), returnMessageProperties.getChannelId(), e);
		}
	}

	private boolean calculateReturnAddress(MessageProperties messageProperties, MessageProperties returnMessageProperties) {
		boolean broadcast = false;
		returnMessageProperties.setPrimary(true);
		returnMessageProperties.setSourceAddress(messageProperties.getDestinationAddress());
		returnMessageProperties.setDestinationAddress(messageProperties.getSourceAddress());
		returnMessageProperties.setTimeReceived(messageProperties.getTimeReceived());
		returnMessageProperties.setChannelId(messageProperties.getChannelId());
		
		if (messageProperties.getSourceAddress() >= 65533) {
			messageProperties.setSourceAddress(address);
			broadcast = true;
		}
		return broadcast;
	}

	private void validateState() {
		if (applicationTransport == null) {
			throw new IllegalStateException("No ApplicationTransport has been defined.");
		}
		if (encoder == null) {
			throw new IllegalStateException("An encoder must be specified.");
		}
		if (decoder == null) {
			throw new IllegalStateException("A decoder must be specified.");
		}
	}

	private void sendData(MessageProperties messageProperties, List<Byte> data, MessageProperties returnMessageProperties, ApplicationFragmentResponse response) {
		applicationTransport.sendData(returnMessageProperties, unmodifiableList(encoder.encode(response)));
	}

	public void setPrimaryAddress(int address) {
		this.address  = address;
	}

	public int getMtu() {
		return mtu;
	}

	public void setMtu(int mtu) {
		this.mtu = mtu;
	}
}
