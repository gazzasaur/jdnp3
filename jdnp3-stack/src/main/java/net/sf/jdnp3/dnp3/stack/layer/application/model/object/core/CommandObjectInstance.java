package net.sf.jdnp3.dnp3.stack.layer.application.model.object.core;

import net.sf.jdnp3.dnp3.stack.layer.application.model.object.binary.StatusCode;


public interface CommandObjectInstance extends ObjectInstance {
	public StatusCode getStatusCode();
}
