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
package net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet;

public enum FunctionCode {
	CONFIRM(0x00),
	READ(0x01),
	WRITE(0x02),
	SELECT(0x03),
	OPERATE(0x04),
	DIRECT_OPERATE(0x05),
	DIRECT_OPERATE_NR(0x06),
	IMMED_FREEZE(0x07),
	IMMED_FREEZE_NR(0x08),
	FREEZE_CLEAR(0x09),
	FREEZE_CLEAR_NR(0x0A),
	FREEZE_AT_TIME(0x0B),
	FREEZE_AT_TIME_NR(0x0C),
	COLD_RESTART(0x0D),
	WARM_RESTART(0x0E),
	INITIALIZE_DATA(0x0F),
	INITIALIZE_APPL(0x10),
	START_APPL(0x11),
	STOP_APPL(0x12),
	SAVE_CONFIG(0x13),
	ENABLE_UNSOLICITED(0x14),
	DISABLE_UNSOLICITED(0x15),
	ASSIGN_CLASS(0x16),
	DELAY_MEASURE(0x17),
	RECORD_CURRENT_TIME(0x18),
	OPEN_FILE(0x19),
	CLOSE_FILE(0x1A),
	DELETE_FILE(0x1B),
	GET_FILE_INFO(0x1C),
	AUTHENTICATE_FILE(0x1D),
	ABORT_FILE(0x1E),
	ACTIVATE_CONFIG(0x1F),
	AUTHENTICATE_REQ(0x20),
	AUTH_REQ_NO_ACK(0x21),
	
	RESPONSE(0x81),
	UNSOLICITED_RESPONSE(0x82),
	AUTHENTICATE_RESP(0x83);
	
	private final int code;
	
	FunctionCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}
	
	public boolean isRequestCode() {
		return code < 129;
	}
}
