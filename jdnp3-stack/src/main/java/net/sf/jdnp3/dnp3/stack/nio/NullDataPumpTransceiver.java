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
package net.sf.jdnp3.dnp3.stack.nio;

import java.nio.channels.SelectableChannel;

public class NullDataPumpTransceiver implements DataPumpTransceiver {
	public void read(SelectableChannel selectableChannel, DataPumpItem dataPumpItem) {
		throw new UnsupportedOperationException();
	}

	public boolean write(SelectableChannel selectableChannel, DataPumpItem dataPumpItem) {
		throw new UnsupportedOperationException();
	}
}
