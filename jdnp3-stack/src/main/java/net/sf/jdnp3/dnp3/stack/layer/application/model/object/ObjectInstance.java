package net.sf.jdnp3.dnp3.stack.layer.application.model.object;

import net.sf.jdnp3.dnp3.stack.layer.application.message.model.packet.ObjectType;

public interface ObjectInstance {
	public long getIndex();
	public ObjectType getRequestedType();
	public void setRequestedType(ObjectType requestedType);
}
