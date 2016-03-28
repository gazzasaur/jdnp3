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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.datalink;

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class OutstationBindingsMessage implements Message {
	private String type = "bindings";
	
	private String site = "";
	private String device = "";
	private List<OutstationBinding> outstationBindings = new ArrayList<OutstationBinding>();
	
	public String getType() {
		return type;
	}

	public List<OutstationBinding> getOutstationBindings() {
		return unmodifiableList(outstationBindings);
	}

	public void setOutstationBindings(List<OutstationBinding> outstationBindings) {
		this.outstationBindings = new ArrayList<>(outstationBindings);
	}

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
}
