package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.Message;

public interface DeviceManager {
    public void sendMessage(Message message);
	public DatabaseManager getDatabaseManager();
}
