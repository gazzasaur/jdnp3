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
package net.sf.jdnp3.dnp3.stack.layer.datalink.model;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;

import net.sf.jdnp3.test.util.list.RandomByteListUtils;

public class DataLinkFrameTest {
	private List<Byte> randomData;
	private DataLinkFrameHeader dataLinkFrameHeader;
	
	@Before
	public void setup() {
		randomData = RandomByteListUtils.create();
		dataLinkFrameHeader = new DataLinkFrameHeader();
	}
	
	@Test
	public void testHeaderValue() {
		DataLinkFrame subject = new DataLinkFrame();
		assertThat(subject.getDataLinkFrameHeader(), notNullValue());
		subject.setDataLinkFrameHeader(dataLinkFrameHeader);
		assertThat(subject.getDataLinkFrameHeader(), sameInstance(dataLinkFrameHeader));
	}
	
	@Test
	public void testDataInitialValue() {
		DataLinkFrame subject = new DataLinkFrame();
		assertThat(subject.getData(), notNullValue());
		assertThat(subject.getData().size(), is(0));
		assertThat(subject.getData().getClass().getCanonicalName(), CoreMatchers.is("java.util.Collections.UnmodifiableRandomAccessList"));
	}
	
	@Test
	public void testDataNewValue() {
		DataLinkFrame subject = new DataLinkFrame();
		subject.setData(randomData);
		assertThat(subject.getData(), equalTo(randomData));
		assertThat(subject.getData().size(), is(randomData.size()));
		assertThat(subject.getData(), not(sameInstance(randomData)));
		assertThat(subject.getData().getClass().getCanonicalName(), CoreMatchers.is("java.util.Collections.UnmodifiableRandomAccessList"));
		RandomByteListUtils.randomise(randomData);
		assertThat(subject.getData(), not(equalTo(randomData)));
	}
}
