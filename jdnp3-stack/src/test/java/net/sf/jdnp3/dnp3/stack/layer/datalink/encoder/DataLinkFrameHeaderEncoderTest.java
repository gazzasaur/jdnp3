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
package net.sf.jdnp3.dnp3.stack.layer.datalink.encoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import mockit.Injectable;
import mockit.Mocked;
import mockit.NonStrictExpectations;
import mockit.StrictExpectations;
import mockit.integration.junit4.JMockit;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.Crc16;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils;
import net.sf.jdnp3.dnp3.stack.utils.DataUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class DataLinkFrameHeaderEncoderTest {
	Random random = new Random();
	
	@Injectable private List<Byte> mockData;
	@Injectable private DataLinkFrameHeader mockDataLinkFrameHeader;
	
	@Mocked private Crc16 mockCrc16;
	@Mocked private DataUtils mockDataUtils;
	@Mocked private DataLinkFrameUtils mockDataLinkFrameUtils;
	
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
		new NonStrictExpectations() {{
			mockDataLinkFrameHeader.getLength(); result = randomLength;
			mockDataLinkFrameHeader.getSource(); result = randomSource;
			mockDataLinkFrameHeader.getDestination(); result = randomDestination;
			DataLinkFrameUtils.computeControlField(mockDataLinkFrameHeader); result = randomControlField;
		}};
		new StrictExpectations() {
			@Mocked ArrayList<Byte> mockArrayList;
		{
			ArrayList<Byte> mockBuffer = new ArrayList<Byte>();
			DataUtils.addInteger(0x6405, 2, mockBuffer);
			DataUtils.addInteger(randomLength, 1, mockBuffer);
			DataUtils.addInteger(randomControlField, 1, mockBuffer);
			DataUtils.addInteger(randomDestination, 2, mockBuffer);
			DataUtils.addInteger(randomSource, 2, mockBuffer);
			mockData.addAll(mockBuffer);
			Crc16.computeCrc(mockBuffer); times = 1; result = randomCrc;
			DataUtils.addInteger(randomCrc, 2, mockData);
		}};
		DataLinkFrameHeaderEncoder.encode(mockDataLinkFrameHeader, mockData);
	}
}
