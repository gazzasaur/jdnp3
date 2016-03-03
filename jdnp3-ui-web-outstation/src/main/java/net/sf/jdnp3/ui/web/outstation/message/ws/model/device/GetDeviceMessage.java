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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.device;

import java.util.ArrayList;
import java.util.List;

import net.sf.jdnp3.ui.web.outstation.channel.OutstationBinding;
import net.sf.jdnp3.ui.web.outstation.database.device.InternalIndicatorsDataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.analog.AnalogOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryInputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryOutputMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.counter.CounterMessage;

public class GetDeviceMessage implements DeviceMessage {
	private String type = "getDevice";
	private String site = "";
	private String device = "";
	
	private InternalIndicatorsDataPoint internalIndicators = new InternalIndicatorsDataPoint();
	private List<BinaryInputMessage> binaryInputPoints = new ArrayList<>();
	private List<BinaryOutputMessage> binaryOutputPoints = new ArrayList<>();
	private List<AnalogInputMessage> analogInputPoints = new ArrayList<>();
	private List<AnalogOutputMessage> analogOutputPoints = new ArrayList<>();
	private List<CounterMessage> counterPoints = new ArrayList<>();
	private List<OutstationBinding> outstationBindings = new ArrayList<>();
	
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

	public List<AnalogOutputMessage> getAnalogOutputPoints() {
		return analogOutputPoints;
	}

	public void setAnalogOutputPoints(List<AnalogOutputMessage> analogOutputPoints) {
		this.analogOutputPoints = analogOutputPoints;
	}

	public InternalIndicatorsDataPoint getInternalIndicators() {
		return internalIndicators;
	}

	public void setInternalIndicators(InternalIndicatorsDataPoint internalIndicators) {
		this.internalIndicators = internalIndicators;
	}

	public List<CounterMessage> getCounterPoints() {
		return counterPoints;
	}

	public void setCounterPoints(List<CounterMessage> counterPoints) {
		this.counterPoints = counterPoints;
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

	public List<OutstationBinding> getOutstationBindings() {
		return outstationBindings;
	}

	public void setOutstationBindings(List<OutstationBinding> outstationBindings) {
		this.outstationBindings = outstationBindings;
	}
}
