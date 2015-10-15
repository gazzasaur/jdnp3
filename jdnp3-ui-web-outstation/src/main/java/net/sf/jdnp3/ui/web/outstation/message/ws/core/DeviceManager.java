package net.sf.jdnp3.ui.web.outstation.message.ws.core;

import net.sf.jdnp3.ui.web.outstation.database.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

public interface DeviceManager {
    public void sendMessage(Message message);
	public DatabaseManager getDatabaseManager();
}
