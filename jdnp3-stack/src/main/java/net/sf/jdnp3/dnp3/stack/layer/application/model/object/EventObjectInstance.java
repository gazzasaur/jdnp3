package net.sf.jdnp3.dnp3.stack.layer.application.model.object;

public interface EventObjectInstance extends ObjectInstance {
	public int getEventClass();
	public long getTimestamp();
}
