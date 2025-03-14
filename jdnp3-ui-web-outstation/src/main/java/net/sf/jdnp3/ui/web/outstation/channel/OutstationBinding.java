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
package net.sf.jdnp3.ui.web.outstation.channel;

public class OutstationBinding {
	private int address = 0;
	private String dataLinkName = "";
	private DataLinkState dataLinkState = DataLinkState.UNKNOWN;

	public int getAddress() {
		return address;
	}

	public void setAddress(int address) {
		this.address = address;
	}

	public String getDataLinkName() {
		return dataLinkName;
	}

	public void setDataLinkName(String dataLinkName) {
		this.dataLinkName = dataLinkName;
	}
	
	public DataLinkState getDataLinkState() {
		return dataLinkState;
	}

	public void setDataLinkState(DataLinkState dataLinkState) {
		this.dataLinkState = dataLinkState;
	}
}
