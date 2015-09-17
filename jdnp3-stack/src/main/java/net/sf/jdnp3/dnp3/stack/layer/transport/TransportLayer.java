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
package net.sf.jdnp3.dnp3.stack.layer.transport;

import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.service.ApplicationLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

public interface TransportLayer extends DataLinkListener {
	public void sendData(MessageProperties messageProperties, List<Byte> data);
	public void receiveData(MessageProperties messageProperties, List<Byte> data);
	
	public int getMaximumReceiveDataSize();
	public void setMaximumReceiveDataSize(int size);
	
	public int getMaximumTransmissionUnit();
	public void setMaximumTransmissionUnit(int size);
	
	public void setDataLinkLater(DataLinkLayer dataLinkLayer);
	public void setApplicationLayer(ApplicationLayer applicationLayer);
}
