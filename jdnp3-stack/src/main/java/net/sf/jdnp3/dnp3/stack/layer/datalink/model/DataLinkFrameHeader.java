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
package net.sf.jdnp3.dnp3.stack.layer.datalink.model;

public class DataLinkFrameHeader {
	private int length = 0;
	private int source = 0;
	private int checkSum = 0;
	private int destination = 0;
	private boolean primary = true;
	private Direction direction = Direction.MASTER_TO_OUTSTATION;
	private FunctionCode functionCode = FunctionCode.UNCONFIRMED_USER_DATA;

	// Frame Check Bit
	private boolean fcb = false;

	// Frame Check Valid for message from PRI or Data Flow Control if from SEC.
	private boolean fcvDfc = false;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public boolean isPrimary() {
		return primary;
	}

	public void setPrimary(boolean primary) {
		this.primary = primary;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setDirection(Direction direction) {
		this.direction = direction;
	}

	public FunctionCode getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(FunctionCode functionCode) {
		this.functionCode = functionCode;
	}

	public int getSource() {
		return source;
	}

	public void setSource(int source) {
		this.source = source;
	}

	public int getDestination() {
		return destination;
	}

	public void setDestination(int destination) {
		this.destination = destination;
	}

	public int getCheckSum() {
		return checkSum;
	}

	public void setCheckSum(int checkSum) {
		this.checkSum = checkSum;
	}

	public boolean isFcb() {
		return fcb;
	}

	public void setFcb(boolean fcb) {
		this.fcb = fcb;
	}

	public boolean isFcvDfc() {
		return fcvDfc;
	}

	public void setFcvDfc(boolean fcvDfc) {
		this.fcvDfc = fcvDfc;
	}
}
