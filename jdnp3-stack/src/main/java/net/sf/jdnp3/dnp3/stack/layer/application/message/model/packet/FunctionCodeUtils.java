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

import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.DIRECT_OPERATE_NR;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.OPERATE;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.SELECT;
import static net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.FunctionCode.WRITE;

import java.util.Arrays;
import java.util.List;

public class FunctionCodeUtils {
	private static final List<FunctionCode> expectsData = Arrays.asList(WRITE, SELECT, OPERATE, DIRECT_OPERATE, DIRECT_OPERATE_NR);
	
	public static boolean hasObjectInstanceData(FunctionCode functionCode) {
		return expectsData.contains(functionCode);
	}
}
