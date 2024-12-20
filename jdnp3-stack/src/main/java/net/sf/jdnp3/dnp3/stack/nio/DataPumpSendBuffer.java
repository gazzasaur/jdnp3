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

import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

import java.nio.ByteBuffer;
import java.util.ArrayDeque;
import java.util.Deque;

public class DataPumpSendBuffer {
	private int maxBufferSize = 0;
	private ByteBuffer sendBuffer;
	private int currentCacheBufferSize = 0;
	private Deque<Deque<Byte>> sendCacheBuffer = new ArrayDeque<>();
	
	public DataPumpSendBuffer(int maxBufferSize) {
		this.maxBufferSize = maxBufferSize;
	}
	
	public synchronized ByteBuffer getSendBuffer() {
		if (sendBuffer != null && sendBuffer.remaining() > 0) {
			return sendBuffer;
		} else if ((sendBuffer == null || sendBuffer.remaining() == 0) && sendCacheBuffer.size() > 0) {
			Deque<Byte> newData = sendCacheBuffer.pollFirst();
			sendBuffer = ByteBuffer.wrap(toPrimitive(newData.toArray(new Byte[0])));
			currentCacheBufferSize -= newData.size();
			return sendBuffer;
		} else {
			return ByteBuffer.allocate(0);
		}
	}
	
	public synchronized void addData(Deque<Byte> data) {
		int freeSpace = maxBufferSize - currentCacheBufferSize;
		if (data.size() <= freeSpace) {
			sendCacheBuffer.add(data);
			currentCacheBufferSize += data.size();
		} else {
			throw new RuntimeException(format("Not enough free space in buffer.  Buffer Size: %s, Free Space: %s, Data Size %s.", maxBufferSize, freeSpace, data.size()));
		}
	}
}
