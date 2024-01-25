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
package net.sf.jdnp3.dnp3.stack.layer.datalink.util;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.OUTSTATION_TO_MASTER;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode.UNCONFIRMED_USER_DATA;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils.computeControlField;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Random;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;

import org.junit.Test;

public class DataLinkFrameUtilsTest {
	private Random random = new Random();
	
	@Test
	public void testConstructor() {
		new DataLinkFrameUtils();
	}
	
	@Test
	public void testCrcGeneration() throws Exception {
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(-100), is(-107));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(-10), is(-5));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(0), is(5));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(10), is(17));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(20), is(27));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(21), is(28));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(22), is(31));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(50), is(61));
		assertThat(DataLinkFrameUtils.headerLengthToRawLength(100), is(117));
	}
	
	@Test
	public void testComputeControlField() {
		performControlFieldTest(MASTER_TO_OUTSTATION, true, 0xC4);
		performControlFieldTest(MASTER_TO_OUTSTATION, false, 0x84);
		performControlFieldTest(OUTSTATION_TO_MASTER, true, 0x44);
		performControlFieldTest(OUTSTATION_TO_MASTER, false, 0x04);
	}
	
	private void performControlFieldTest(Direction direction, boolean primary, int expectedControlField) {
		DataLinkFrameHeader header = createRandomHeader();
		header.setPrimary(primary);
		header.setDirection(direction);
		assertThat(computeControlField(header), is((byte) expectedControlField));
	}

	private DataLinkFrameHeader createRandomHeader() {
		DataLinkFrameHeader header = new DataLinkFrameHeader();
		header.setLength(random.nextInt());
		header.setSource(random.nextInt());
		header.setCheckSum(random.nextInt());
		header.setDestination(random.nextInt());
		header.setFunctionCode(UNCONFIRMED_USER_DATA);
		return header;
	}
}
