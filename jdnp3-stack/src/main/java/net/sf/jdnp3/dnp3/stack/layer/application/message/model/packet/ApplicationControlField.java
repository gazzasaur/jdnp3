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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

public class ApplicationControlField {
	private boolean firstFragmentOfMessage = false;
	private boolean finalFragmentOfMessage = false;
	private boolean confirmationRequired = false;
	private boolean unsolicitedResponse = false;
	private int sequenceNumber = -1;
	
	public boolean isFirstFragmentOfMessage() {
		return firstFragmentOfMessage;
	}
	
	public void setFirstFragmentOfMessage(boolean firstFragmentOfMessage) {
		this.firstFragmentOfMessage = firstFragmentOfMessage;
	}

	public boolean isFinalFragmentOfMessage() {
		return finalFragmentOfMessage;
	}

	public void setFinalFragmentOfMessage(boolean finalFragmentOfMessage) {
		this.finalFragmentOfMessage = finalFragmentOfMessage;
	}

	public boolean isConfirmationRequired() {
		return confirmationRequired;
	}

	public void setConfirmationRequired(boolean confirmationRequired) {
		this.confirmationRequired = confirmationRequired;
	}

	public boolean isUnsolicitedResponse() {
		return unsolicitedResponse;
	}

	public void setUnsolicitedResponse(boolean unsolicitedResponse) {
		this.unsolicitedResponse = unsolicitedResponse;
	}

	public int getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(int sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}
}
