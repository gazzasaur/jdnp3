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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

public class ApplicationFragmentResponseHeader {
	private FunctionCode functionCode = FunctionCode.CONFIRM;
	private ApplicationControlField applicationControl = new ApplicationControlField();
	private InternalIndicatorField internalIndicatorField = new InternalIndicatorField();
	
	public FunctionCode getFunctionCode() {
		return functionCode;
	}
	
	public void setFunctionCode(FunctionCode functionCode) {
		this.functionCode = functionCode;
	}

	public ApplicationControlField getApplicationControl() {
		return applicationControl;
	}

	public void setApplicationControl(ApplicationControlField applicationControl) {
		this.applicationControl = applicationControl;
	}

	public InternalIndicatorField getInternalIndicatorField() {
		return internalIndicatorField;
	}

	public void setInternalIndicatorField(InternalIndicatorField internalIndicatorField) {
		this.internalIndicatorField = internalIndicatorField;
	}
}
