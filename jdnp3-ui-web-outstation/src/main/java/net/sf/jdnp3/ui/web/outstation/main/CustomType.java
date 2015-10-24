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
package net.sf.jdnp3.ui.web.outstation.main;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.NoPrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.prefix.PrefixType;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.NoRange;
import net.sf.jdnp3.dnp3.stack.layer.application.message.model.range.Range;

public class CustomType {
	private String expectedData = "";
	private String responseData = "";
	private ObjectType objectType = new ObjectType(-1, -1);
	private FunctionCode functionCode = FunctionCode.WRITE;
	private Class<? extends Range> rangeClass = NoRange.class;
	private Class<? extends PrefixType> prefixTypeClass = NoPrefixType.class;
	
	public String getExpectedData() {
		return expectedData;
	}
	
	public void setExpectedData(String expectedData) {
		this.expectedData = expectedData;
	}

	public String getResponseData() {
		return responseData;
	}

	public void setResponseData(String responseData) {
		this.responseData = responseData;
	}

	public ObjectType getObjectType() {
		return objectType;
	}

	public void setObjectType(ObjectType objectType) {
		this.objectType = objectType;
	}

	public FunctionCode getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(FunctionCode functionCode) {
		this.functionCode = functionCode;
	}

	public Class<? extends Range> getRangeClass() {
		return rangeClass;
	}

	public void setRangeClass(Class<? extends Range> rangeClass) {
		this.rangeClass = rangeClass;
	}

	public Class<? extends PrefixType> getPrefixTypeClass() {
		return prefixTypeClass;
	}

	public void setPrefixTypeClass(Class<? extends PrefixType> prefixTypeClass) {
		this.prefixTypeClass = prefixTypeClass;
	}
}
