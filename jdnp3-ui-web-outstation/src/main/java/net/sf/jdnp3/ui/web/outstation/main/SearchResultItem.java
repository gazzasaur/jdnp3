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
package net.sf.jdnp3.ui.web.outstation.main;

public class SearchResultItem {
	private String site = "";
	private String device = "";
	private String pointType = "";
	private long pointIndex = 0;

	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	public String getDevice() {
		return device;
	}

	public void setDevice(String device) {
		this.device = device;
	}

	public String getPointType() {
		return pointType;
	}
	
	public void setPointType(String pointType) {
		this.pointType = pointType;
	}
	
	public long getPointIndex() {
		return pointIndex;
	}

	public void setPointIndex(long pointIndex) {
		this.pointIndex = pointIndex;
	}	
}
