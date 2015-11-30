package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public interface Messanger {
    public void sendMessage(Message message);
}
