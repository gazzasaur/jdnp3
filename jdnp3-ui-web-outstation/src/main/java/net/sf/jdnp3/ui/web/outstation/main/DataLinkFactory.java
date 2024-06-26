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
package net.sf.jdnp3.ui.web.outstation.main;

import java.util.concurrent.ExecutorService;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.client.TcpClientDataLinkService;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.TcpServerDataLinkService;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManager;
import net.sf.jdnp3.ui.web.outstation.channel.DataLinkManagerProvider;

public class DataLinkFactory {
	private DataPump dataPump;
	private ExecutorService executorService;
	
	public DataLinkManager create(String name, String host, int port) {
		if (dataPump == null || executorService == null) {
			throw new RuntimeException("Must specify a data pump and executor service.");
		}
		
		TcpServerDataLinkService dataLinkLayer = new TcpServerDataLinkService();
		dataLinkLayer.setExecutorService(executorService);
		dataLinkLayer.setDataPump(dataPump);
		dataLinkLayer.setHost(host);
		dataLinkLayer.setPort(port);
		
		DataLinkManager dataLinkManager = DataLinkManagerProvider.registerDataLink(name);
		dataLinkManager.setDataLinkLayer(dataLinkLayer);
		return dataLinkManager;
	}

	public DataLinkManager createClient(String name, String host, int port) {
		if (dataPump == null || executorService == null) {
			throw new RuntimeException("Must specify a data pump and executor service.");
		}
		
		TcpClientDataLinkService dataLinkLayer = new TcpClientDataLinkService(host, port);
		dataLinkLayer.setExecutorService(executorService);
		dataLinkLayer.setDataPump(dataPump);
		
		DataLinkManager dataLinkManager = DataLinkManagerProvider.registerDataLink(name);
		dataLinkManager.setDataLinkLayer(dataLinkLayer);
		return dataLinkManager;
	}

	public void setDataPump(DataPump dataPump) {
		this.dataPump = dataPump;
	}
	
	public void setExecutorService(ExecutorService executorService) {
		this.executorService = executorService;
	}
}
