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
package net.sf.jdnp3.ui.web.outstation.message.ws.model;

import java.util.ArrayList;
import java.util.List;

public class GetDeviceMessage implements Message {
	private String type = "getDevice";
	
	private List<BinaryInputMessage> binaryInputPoints = new ArrayList<>();
	private List<BinaryOutputMessage> binaryOutputPoints = new ArrayList<>();
	private List<AnalogInputMessage> analogInputPoints = new ArrayList<>();
	
	public String getType() {
		return type;
	}

	public List<BinaryInputMessage> getBinaryInputPoints() {
		return binaryInputPoints;
	}

	public void setBinaryInputPoints(List<BinaryInputMessage> binaryInputPoints) {
		this.binaryInputPoints = binaryInputPoints;
	}

	public List<BinaryOutputMessage> getBinaryOutputPoints() {
		return binaryOutputPoints;
	}

	public void setBinaryOutputPoints(List<BinaryOutputMessage> binaryOutputPoints) {
		this.binaryOutputPoints = binaryOutputPoints;
	}

	public List<AnalogInputMessage> getAnalogInputPoints() {
		return analogInputPoints;
	}

	public void setAnalogInputPoints(List<AnalogInputMessage> analogInputPoints) {
		this.analogInputPoints = analogInputPoints;
	}

}