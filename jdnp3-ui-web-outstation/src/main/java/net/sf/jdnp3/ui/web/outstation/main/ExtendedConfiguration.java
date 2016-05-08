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

import static java.util.Collections.unmodifiableList;

import java.util.ArrayList;
import java.util.List;

public class ExtendedConfiguration {
	private List<CustomType> customTypes = new ArrayList<>();
	private List<String> binaryInputPoints = new ArrayList<>();
	private List<String> binaryOutputPoints = new ArrayList<>();
	private List<String> analogInputPoints = new ArrayList<>();
	private List<String> analogOutputPoints = new ArrayList<>();
	private List<String> counterPoints = new ArrayList<>();
	
	public List<CustomType> getCustomTypes() {
		return unmodifiableList(customTypes);
	}

	public void setCustomTypes(List<CustomType> customTypes) {
		this.customTypes = new ArrayList<>(customTypes);
	}

	public List<String> getBinaryInputPoints() {
		return unmodifiableList(binaryInputPoints);
	}

	public void setBinaryInputPoints(List<String> binaryInputPoints) {
		this.binaryInputPoints = new ArrayList<>(binaryInputPoints);
	}

	public List<String> getBinaryOutputPoints() {
		return unmodifiableList(binaryOutputPoints);
	}

	public void setBinaryOutputPoints(List<String> binaryOutputPoints) {
		this.binaryOutputPoints = new ArrayList<>(binaryOutputPoints);
	}

	public List<String> getAnalogInputPoints() {
		return unmodifiableList(analogInputPoints);
	}

	public void setAnalogInputPoints(List<String> analogInputPoints) {
		this.analogInputPoints = new ArrayList<>(analogInputPoints);
	}

	public List<String> getAnalogOutputPoints() {
		return unmodifiableList(analogOutputPoints);
	}

	public void setAnalogOutputPoints(List<String> analogOutputPoints) {
		this.analogOutputPoints = new ArrayList<>(analogOutputPoints);
	}

	public List<String> getCounterPoints() {
		return unmodifiableList(counterPoints);
	}

	public void setCounterPoints(List<String> counterPoints) {
		this.counterPoints = new ArrayList<>(counterPoints);
	}
	
}
