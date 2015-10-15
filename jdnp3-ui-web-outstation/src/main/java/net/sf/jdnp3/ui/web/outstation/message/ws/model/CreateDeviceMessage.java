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

public class CreateDeviceMessage implements Message {
	private String type = "createDevice";
	private String siteCode = "";
	private String deviceCode = "";
	
	private List<String> binaryInputPoints = new ArrayList<>();
	private List<String> binaryOutputPoints = new ArrayList<>();
	private List<String> analogInputPoints = new ArrayList<>();
	
	public String getType() {
		return type;
	}

	public String getSiteCode() {
		return siteCode;
	}

	public void setSiteCode(String siteCode) {
		this.siteCode = siteCode;
	}

	public String getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(String deviceCode) {
		this.deviceCode = deviceCode;
	}

	public List<String> getBinaryInputPoints() {
		return binaryInputPoints;
	}

	public void setBinaryInputPoints(List<String> binaryInputPoints) {
		this.binaryInputPoints = binaryInputPoints;
	}

	public List<String> getBinaryOutputPoints() {
		return binaryOutputPoints;
	}

	public void setBinaryOutputPoints(List<String> binaryOutputPoints) {
		this.binaryOutputPoints = binaryOutputPoints;
	}

	public List<String> getAnalogInputPoints() {
		return analogInputPoints;
	}

	public void setAnalogInputPoints(List<String> analogInputPoints) {
		this.analogInputPoints = analogInputPoints;
	}
}
