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
package net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary;

import static net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.ObjectTypeConstants.ANY;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;
import net.sf.jdnp3.dnp3.stack.layer.application.model.object.core.StaticObjectInstance;

public class BinaryOutputStaticObjectInstance implements StaticObjectInstance {
	private long index = 0;
	private boolean active = false;
	private ObjectType requestedType = ANY;
	
	private boolean online = true;
	private boolean restart = false;
	private boolean localForced = false;
	private boolean remoteForced = false;
	private boolean communicationsLost = false;
	
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

	public ObjectType getRequestedType() {
		return requestedType;
	}

	public void setRequestedType(ObjectType requestedType) {
		this.requestedType = requestedType;
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
}
