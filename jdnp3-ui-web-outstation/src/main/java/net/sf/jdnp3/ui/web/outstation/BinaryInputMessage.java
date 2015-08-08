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
package net.sf.jdnp3.ui.web.outstation;

public class BinaryInputMessage implements Message {
	private String type = "binaryInput";
	private long index = 0;
	private int eventClass = 1;
	private int eventVariation = 0;
	private int staticVariation = 0;
	
	private String name = "";
	private boolean active = false;
	private boolean online = true;
	private boolean restart = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean chatterFilter = false;
	private boolean communicationsLost = false;
	
	public String getType() {
		return type;
	}
	
	public long getIndex() {
		return index;
	}

	public void setIndex(long index) {
		this.index = index;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public boolean isChatterFilter() {
		return chatterFilter;
	}

	public void setChatterFilter(boolean chatterFilter) {
		this.chatterFilter = chatterFilter;
	}

	public boolean isOnline() {
		return online;
	}

	public void setOnline(boolean online) {
		this.online = online;
	}

	public boolean isRestart() {
		return restart;
	}

	public void setRestart(boolean restart) {
		this.restart = restart;
	}

	public boolean isLocalForced() {
		return localForced;
	}

	public void setLocalForced(boolean localForced) {
		this.localForced = localForced;
	}

	public boolean isRemoteForced() {
		return remoteForced;
	}

	public void setRemoteForced(boolean remoteForced) {
		this.remoteForced = remoteForced;
	}

	public boolean isCommunicationsLost() {
		return communicationsLost;
	}

	public void setCommunicationsLost(boolean communicationsLost) {
		this.communicationsLost = communicationsLost;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStaticVariation() {
		return staticVariation;
	}

	public void setStaticVariation(int staticVariation) {
		this.staticVariation = staticVariation;
	}

	/**
	 * @return the eventVariation
	 */
	public int getEventVariation() {
		return eventVariation;
	}

	/**
	 * @param eventVariation the eventVariation to set
	 */
	public void setEventVariation(int eventVariation) {
		this.eventVariation = eventVariation;
	}

	/**
	 * @return the eventClass
	 */
	public int getEventClass() {
		return eventClass;
	}

	/**
	 * @param eventClass the eventClass to set
	 */
	public void setEventClass(int eventClass) {
		this.eventClass = eventClass;
	}
}
