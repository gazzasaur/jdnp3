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
package net.sf.jdnp3.ui.web.outstation.message.ws.handler;

import java.util.Arrays;
import java.util.List;

import net.sf.jdnp3.dnp3.stack.layer.application.service.InternalStatusProvider;
import net.sf.jdnp3.ui.web.outstation.GenericWebSocket;
import net.sf.jdnp3.ui.web.outstation.MessageHandler;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.InternalIndicatorMessage;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InternalIndicatorMessageHandler implements MessageHandler {
	private Logger logger = LoggerFactory.getLogger(InternalIndicatorMessageHandler.class);
	private static final List<String> TRUSTED_ATTRIBUTES = Arrays.asList("broadcast", "class1Events", "class2Events", "class3Events", "needTime", "localControl", "deviceTrouble", "deviceRestart", "noFunctionCodeSupport", "objectUnknown", "parameterError", "eventBufferOverflow", "alreadyExecuting", "configurationCorrupt");
	
	private InternalStatusProvider internalStatusProvider;
	
	public InternalIndicatorMessageHandler(InternalStatusProvider internalStatusProvider) {
		this.internalStatusProvider = internalStatusProvider;
	}
	
	public boolean canHandle(Message message) {
		return message instanceof InternalIndicatorMessage;
	}

	public void processMessage(GenericWebSocket genericWebSocket, Message message) {
		if (!this.canHandle(message)) {
			throw new IllegalArgumentException("Cannot handle message of type " + message.getClass());
		}
		InternalIndicatorMessage specificMessage = (InternalIndicatorMessage) message;
		
		if (!TRUSTED_ATTRIBUTES.contains(specificMessage.getAttribute())) {
			logger.warn("An attempt was made to set an untrusted attribute: " + specificMessage.getAttribute());
			return;
		}
		
		try {
			BeanUtils.setProperty(internalStatusProvider, specificMessage.getAttribute(), specificMessage.isValue());
		} catch (Exception e) {
			logger.error("Failed to set IIN flag.", e);
		}
	}
}