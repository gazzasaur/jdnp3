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
package net.sf.jdnp3.ui.web.outstation.message.ws.model.datalink;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public class CreateDataLinkMessage implements Message {
	private String type = "createDataLink";
	private String dataLinkFactory = "";
	private String host = "0.0.0.0";
	private String dataLink = "";
	private int port = 20000;
	
	public String getType() {
		return type;
	}

	public String getDataLink() {
		return dataLink;
	}

	public void setDataLink(String dataLink) {
		this.dataLink = dataLink;
	}

	public String getDataLinkFactory() {
		return dataLinkFactory;
	}

	public void setDataLinkFactory(String dataLinkFactory) {
		this.dataLinkFactory = dataLinkFactory;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
