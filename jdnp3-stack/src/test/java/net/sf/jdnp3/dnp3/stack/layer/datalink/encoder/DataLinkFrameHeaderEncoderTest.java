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
package net.sf.jdnp3.dnp3.stack.layer.datalink.encoder;

import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

import java.util.LinkedList;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

@RunWith(MockitoJUnitRunner.class)
public class DataLinkFrameHeaderEncoderTest {
	Random random = new Random();
	
	@Mock private DataLinkFrameHeader mockDataLinkFrameHeader;
	
	@Mock private DataUtils mockDataUtils;
	@Mock private DataLinkFrameUtils mockDataLinkFrameUtils;
	
	private int randomCrc;
	private int randomLength;
	private int randomSource;
	private int randomDestination;
	private byte randomControlField;

	@Before
	public void setup() {
		randomCrc = random.nextInt();
		randomLength = random.nextInt();
		randomSource = random.nextInt();
		randomDestination = random.nextInt();
		randomControlField = (byte) random.nextInt();
	}

	@Test
	public void testConstructor() {
		new DataLinkFrameHeaderEncoder();
	}
	
	@Test
	public void testEncodeHeader() {
		when(mockDataLinkFrameHeader.getLength()).thenReturn(randomLength);
		when(mockDataLinkFrameHeader.getSource()).thenReturn(randomSource);
		when(mockDataLinkFrameHeader.getDestination()).thenReturn(randomDestination);

		LinkedList<Byte> buffer = new LinkedList<Byte>();
		DataUtils.addInteger(0x6405, 2, buffer);
		DataUtils.addInteger(randomLength, 1, buffer);
		DataUtils.addInteger(randomControlField, 1, buffer);
		DataUtils.addInteger(randomDestination, 2, buffer);
		DataUtils.addInteger(randomSource, 2, buffer);
		LinkedList<Byte> data = new LinkedList<>(buffer);

		try (MockedStatic<Crc16> staticCrc16 = mockStatic(Crc16.class); MockedStatic<DataLinkFrameUtils> staticDataLinkFrameUtils = mockStatic(DataLinkFrameUtils.class)) {
			staticCrc16.when(() -> Crc16.computeCrc(buffer)).thenReturn(randomCrc);
			staticDataLinkFrameUtils.when(() -> DataLinkFrameUtils.computeControlField(mockDataLinkFrameHeader)).thenReturn(randomControlField);

			DataLinkFrameHeaderEncoder.encode(mockDataLinkFrameHeader, data);
		}
	}
}
