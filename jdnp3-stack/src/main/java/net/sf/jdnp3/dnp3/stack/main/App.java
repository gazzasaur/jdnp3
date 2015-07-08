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
package net.sf.jdnp3.dnp3.stack.main;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.application.message.decoder.packet.ApplicationFragmentRequestDecoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.encoder.packet.ApplicationFragmentResponseEncoderImpl;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentRequest;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponse;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ApplicationFragmentResponseHeader;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectField;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectFragment;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectPrefixCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.IndexRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.RangeSpecifierCode;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.BinaryInputStaticObjectInstance;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.ObjectTypeConstants;
import net.sf.jdnp3.dnp3.stack.layer.datalink.io.TcpIpServerDataLink;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayer;
import net.sf.jdnp3.dnp3.stack.layer.transport.TransportLayerImpl;


public class App {
	public static void main(String[] args) throws InterruptedException {
		TransportLayer transport = new TransportLayerImpl();
		transport.setApplicationLayer(new ApplicationLayer() {
			boolean deviceReset = true;
			public void dataReceived(List<Byte> data) {
				System.out.print("AL: ");
				for (Byte value : data) {
					System.out.print(String.format("%02X ", value));
				}
				System.out.println();
				
				ApplicationFragmentRequestDecoderImpl decoder = new ApplicationFragmentRequestDecoderImpl();
				ApplicationFragmentRequest request = decoder.decode(data);
				
				ApplicationFragmentResponse fragment = new ApplicationFragmentResponse();
				ApplicationFragmentResponseHeader applicationResponseHeader = new ApplicationFragmentResponseHeader();
				applicationResponseHeader.setFunctionCode(FunctionCode.RESPONSE);
				applicationResponseHeader.getInternalIndicatorField().setDeviceRestart(deviceReset);
				applicationResponseHeader.getApplicationControl().setConfirmationRequired(false);
				applicationResponseHeader.getApplicationControl().setFirstFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setFinalFragmentOfMessage(true);
				applicationResponseHeader.getApplicationControl().setUnsolicitedResponse(false);
				applicationResponseHeader.getApplicationControl().setSequenceNumber(request.getHeader().getApplicationControl().getSequenceNumber());
				fragment.setHeader(applicationResponseHeader);
				
				ObjectFragment objectFragment = new ObjectFragment();
				IndexRange indexRange = new IndexRange();
				indexRange.setStartIndex(0);
				indexRange.setStopIndex(15);
				objectFragment.getObjectFragmentHeader().setObjectType(ObjectTypeConstants.BINARY_INPUT_STATIC_PACKED);
				objectFragment.getObjectFragmentHeader().getQualifierField().setObjectPrefixCode(ObjectPrefixCode.NONE);
				objectFragment.getObjectFragmentHeader().getQualifierField().setRangeSpecifierCode(RangeSpecifierCode.ONE_OCTET_INDEX);
				objectFragment.getObjectFragmentHeader().setRange(indexRange);
				
				for (int i = 0; i < 16; ++i) {
					BinaryInputStaticObjectInstance binary = new BinaryInputStaticObjectInstance();
					binary.setIndex(i);
					binary.setActive(i%2 > 0);
					ObjectField objectField = new ObjectField();
					objectField.setPrefix(i);
					objectField.setObjectInstance(binary);
					objectFragment.addObjectField(objectField);
				}
				
				fragment.addObjectFragment(objectFragment);
				
				transport.sendData(new ApplicationFragmentResponseEncoderImpl().encode(fragment));
				deviceReset = false;
			}
		});
		
		TcpIpServerDataLink dataLink = new TcpIpServerDataLink();
		dataLink.setDirection(Direction.OUTSTATION_TO_MASTER);
		dataLink.setTransportLayer(transport);
		dataLink.setDestination(1);
		dataLink.setSource(2);
		
		transport.setDataLinkLater(dataLink);
		
		dataLink.enable();
		
		Thread.sleep(60000);
		System.exit(0);
		
//		Master master = new Master();
		
//		ApplicationFragmentRequest fragment = new ApplicationFragmentRequest();
//		ApplicationFragmentRequestHeader applicationRequestHeader = new ApplicationFragmentRequestHeader();
//		applicationRequestHeader.setFunctionCode(FunctionCode.WRITE);
//		applicationRequestHeader.getApplicationControl().setConfirmationRequired(false);
//		applicationRequestHeader.getApplicationControl().setFirstFragmentOfMessage(true);
//		applicationRequestHeader.getApplicationControl().setFinalFragmentOfMessage(true);
//		applicationRequestHeader.getApplicationControl().setUnsolicitedResponse(false);
//		applicationRequestHeader.getApplicationControl().setSequenceNumber(1);
//		fragment.setApplicationHeader(applicationRequestHeader);
//		
//		OneOctetIndexRange range = new OneOctetIndexRange();
//		range.setStartIndex(7);
//		range.setStopIndex(7);
//		
//		ObjectFragment objectFragment = new ObjectFragment();
//		objectFragment.getObjectFragmentHeader().getObjectType().setGroup(80);
//		objectFragment.getObjectFragmentHeader().getObjectType().setVariation(1);
//		objectFragment.getObjectFragmentHeader().getQualifierField().setObjectPrefixCode(ObjectPrefixCode.NONE);
//		objectFragment.getObjectFragmentHeader().getQualifierField().setRangeSpecifierCode(RangeSpecifierCode.ONE_OCTET_INDEX);
//		objectFragment.getObjectFragmentHeader().setRange(range);
//		
//		OneOctetObjectInstance oneOctetObjectInstance = new OneOctetObjectInstance();
//		oneOctetObjectInstance.setValue(0);
//		
//		ObjectField objectField = new ObjectField();
//		objectField.setObjectPrefix(new NoObjectPrefix());
//		objectField.setObjectInstance(oneOctetObjectInstance);
//		objectFragment.addObjectField(objectField);
//		fragment.addObjectFragment(objectFragment);
//		
//		ApplicationFragmentEncoder fragmentEncoder = new ApplicationFragmentEncoderImpl();
//		for (Byte data : fragmentEncoder.encode(fragment)) {
//			System.out.print(String.format("%02X ", data));
//		}
//		System.out.println();
//		
//		TransportSegment transportSegment = new TransportSegment();
//		transportSegment.getTransportHeader().setFirstSegment(true);
//		transportSegment.getTransportHeader().setFinalSegment(true);
//		transportSegment.getTransportHeader().setSequenceNumber(2);
//		transportSegment.setData(fragmentEncoder.encode(fragment));
//		
//		TransportSegmentEncoder transportSegmentEncoder = new TransportSegmentEncoderImpl();
//		for (Byte data : transportSegmentEncoder.encode(transportSegment)) {
//			System.out.print(String.format("%02X ", data));
//		}
//		System.out.println();
//		
//		DataLinkFrame dataLinkFrame = new DataLinkFrame();
//		dataLinkFrame.setData(transportSegmentEncoder.encode(transportSegment));
//		dataLinkFrame.getDataLinkFrameHeader().setSource(1);
//		dataLinkFrame.getDataLinkFrameHeader().setLength(5 + dataLinkFrame.getData().size());
//		dataLinkFrame.getDataLinkFrameHeader().setDestination(10);
//		
//		DataLinkFrameEncoder dataLinkFrameEncoder = new DataLinkFrameEncoderImpl();
//		for (Byte data : dataLinkFrameEncoder.encode(dataLinkFrame)) {
//			System.out.print(String.format("%02X ", data));
//		}
	}
}
