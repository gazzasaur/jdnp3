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

import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import static org.apache.commons.lang3.ArrayUtils.toObject;

import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayDeque;
import java.util.Deque;

public class SocketChannelDataPumpTransceiver implements DataPumpTransceiver {
	public boolean read(SelectableChannel selectableChannel, DataPumpItem dataPumpItem) {
		SocketChannel socketChannel = (SocketChannel) selectableChannel;
		
		int count = 0;
		ByteBuffer byteBuffer = ByteBuffer.allocate(dataPumpItem.getMaxBufferSize());
		
		try {
			count = socketChannel.read(byteBuffer);
			if (count < 0) {
				return false;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		if (count != 0) {
			Deque<Byte> data = new ArrayDeque<>(asList(toObject(copyOf(byteBuffer.array(), count))));
			dataPumpItem.getDataPumpListener().dataReceived(data);
		}
		return true;
	}

	public boolean write(SelectableChannel selectableChannel, DataPumpItem dataPumpItem) {
		SocketChannel socketChannel = (SocketChannel) selectableChannel;
		
		ByteBuffer sendBuffer = dataPumpItem.getDataPumpSendBuffer().getSendBuffer();
		if (!sendBuffer.hasRemaining()) {
			return false;
		}
		
		try {
			socketChannel.write(sendBuffer);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return true;
	}
}
