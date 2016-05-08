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

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode.UNCONFIRMED_USER_DATA;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import mockit.integration.junit4.JMockit;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(JMockit.class)
public class FunctionCodeTest {
	@Test
	public void testEnumCoverage() {
		FunctionCode.valueOf("UNCONFIRMED_USER_DATA");
	}
	
	@Test
	public void testFunctionCodeValues() {
		assertThat(UNCONFIRMED_USER_DATA.getCode(), is(4));
	}
}