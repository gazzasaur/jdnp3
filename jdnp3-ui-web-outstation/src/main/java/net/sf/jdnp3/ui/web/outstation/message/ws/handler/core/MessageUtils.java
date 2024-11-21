package net.sf.jdnp3.ui.web.outstation.message.ws.handler.core;

import java.beans.PropertyDescriptor;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.beanutils.PropertyUtilsBean;

import net.sf.jdnp3.ui.web.outstation.database.core.DataPoint;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.core.DeviceMessage;

public class MessageUtils {
    private static final PropertyUtilsBean PROPERTY_UTILS_BEAN = new PropertyUtilsBean();
    private static final Set<String> IGNORE_PROPERTIES = new HashSet<String>(Arrays.asList("index", "site", "device", "name", "type", "tags"));

    public static boolean copyProperties(DataPoint destination, DeviceMessage source) throws Exception {
        boolean modified = false;
        PropertyDescriptor[] destinationDescriptors = PROPERTY_UTILS_BEAN.getPropertyDescriptors(destination);

        for (PropertyDescriptor destinationDescriptor : destinationDescriptors) {
            if (IGNORE_PROPERTIES.contains(destinationDescriptor.getName())) {
                continue;
            }
            Object sourceProperty = PROPERTY_UTILS_BEAN.getPropertyDescriptor(source, destinationDescriptor.getName()).getReadMethod().invoke(source);
            if (sourceProperty != null) {
                modified = true;
                destinationDescriptor.getWriteMethod().invoke(destination, sourceProperty);
            }
        }
        return modified;
    }
}
