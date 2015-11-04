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
package net.sf.jdnp3.ui.web.outstation.ui.web;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.main.DeviceProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.DeviceManager;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.decoder.GenericMessageDecoder;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistry;
import net.sf.jdnp3.ui.web.outstation.message.ws.handler.core.MessageHandlerRegistryProvider;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.FailureMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.SuccessMessage;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@SuppressWarnings("serial")
public class JsonApiServlet extends HttpServlet implements DeviceManager {
	private Logger logger = LoggerFactory.getLogger(JsonApiServlet.class);
	private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().setPrettyPrinting().create();
	
	private Message returnMessage;
	private DatabaseManager databaseManager;
	private boolean siteRequired = false;
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String siteCode = request.getParameter("siteCode");
		String deviceCode = request.getParameter("deviceCode");
		
		if (siteCode != null && siteCode.isEmpty() || deviceCode != null && deviceCode.isEmpty()) {
			generateFailure(response, "A siteCode and deviceCode must not be empty.");
			return;
		}
		try {
			databaseManager = DeviceProvider.getDevice(siteCode, deviceCode).getDatabaseManager();
		} catch (Exception e) {
		}
		
		String jsonData = defaultIfNull(IOUtils.toString(request.getInputStream()), "");
		GenericMessageDecoder decoder = new GenericMessageDecoder();
		Message message;
		try {
			message = decoder.decode(jsonData);
		} catch (Exception e) {
			logger.error("Failed to parse message.", e);
			generateFailure(response, "Failed to parse message.");
			return;
		}
		
		try {
			MessageHandlerRegistry messageHandlerRegistry = MessageHandlerRegistryProvider.getMessageHandlerRegistry();
			MessageHandler messageHandler = messageHandlerRegistry.fetchMessageHandler(message);
			messageHandler.processMessage(this, message);
		} catch (Exception e) {
			String reason = (siteRequired) ? format("Cannot find device %s:%s", siteCode, deviceCode) : "Failed to process message.";
			logger.error("reason", e);
			generateFailure(response, reason);
			return;
		}
		
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		if (returnMessage == null) {
			response.getWriter().println(gson.toJson(new SuccessMessage()));
		} else {
			response.getWriter().println(gson.toJson(returnMessage));
		}
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	private void generateFailure(HttpServletResponse response, String reason) throws IOException {
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		FailureMessage failureMessage = new FailureMessage();
		failureMessage.setReason(reason);
		response.getWriter().println(gson.toJson(failureMessage));
	}

	public void sendMessage(Message message) {
		returnMessage = message;
	}

	public DatabaseManager getDatabaseManager() {
		if (databaseManager == null) {
			siteRequired  = true;
			throw new IllegalStateException("No database is available.");
		}
		return databaseManager;
	}
}
