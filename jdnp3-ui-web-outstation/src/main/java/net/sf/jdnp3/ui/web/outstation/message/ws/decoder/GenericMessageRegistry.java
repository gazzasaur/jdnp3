package net.sf.jdnp3.ui.web.outstation.message.ws.decoder;

import static java.lang.String.format;

import java.util.HashMap;
import java.util.Map;

import net.sf.jdnp3.ui.web.outstation.database.DataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.Message;

public class GenericMessageRegistry {
	private Map<String, Class<? extends Message>> typeMessageMap = new HashMap<>();
	private Map<Class<? extends DataPoint>, Class<? extends Message>> dataPointMessageMap = new HashMap<>();
	
	public void register(String type, Class<? extends Message> messageClass) {
		typeMessageMap.put(type, messageClass);
	}
	
	public void register(String type, Class<? extends DataPoint> dataPointClass, Class<? extends Message> messageClass) {
		typeMessageMap.put(type, messageClass);
		dataPointMessageMap.put(dataPointClass, messageClass);
	}
	
	public boolean isRegistered(String type) {
		return typeMessageMap.containsKey(type);
	}
	
	public boolean isRegistered(Class<? extends DataPoint> dataPointClass) {
		return dataPointMessageMap.containsKey(dataPointClass);
	}

	public Class<? extends Message> get(String type) {
		Class<? extends Message> clazz = typeMessageMap.get(type);
		if (clazz == null) {
			throw new IllegalArgumentException(format("The type %s is not registered.", type));
		}
		return clazz;
	}

	public Class<? extends Message> get(Class<? extends DataPoint> dataPointClass) {
		Class<? extends Message> clazz = dataPointMessageMap.get(dataPointClass);
		if (clazz == null) {
			throw new IllegalArgumentException(format("The class %s is not registered.", dataPointClass));
		}
		return clazz;
	}
}
