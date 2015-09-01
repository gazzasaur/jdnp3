package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

public class GenericMessageRegistryProvider {
	private static GenericMessageRegistry registry = new GenericMessageRegistry();
	
	public static GenericMessageRegistry getRegistry() {
		return registry;
	}
}
