package net.sf.jdnp3.dnp3.stack.layer.datalink.inttest;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Test;

import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.StatefulDataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.TcpServerDataLinkService;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;

public class DataLinkOutstationTest {
    @Test
    public void testUnconfirmedUserData() {
        var testPort = RandomUtils.nextInt(40000, 60000);

        var outstationDataLinkLayer = new TcpServerDataLinkService();
        var outstationDataLinkInterceptor = new StatefulDataLinkInterceptor(false, outstationDataLinkLayer, 1234, (MessageProperties messageProperties, List<Byte> data) -> {
            // Echo the data back.
            var responseProperties = new MessageProperties();
            tryCloneMessageProperties(messageProperties, responseProperties);
            responseProperties.setDestinationAddress(messageProperties.getSourceAddress());
            responseProperties.setSourceAddress(messageProperties.getDestinationAddress());
            outstationDataLinkLayer.sendData(responseProperties, data);
        });
        outstationDataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        outstationDataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        outstationDataLinkLayer.setHost("localhost");
        outstationDataLinkLayer.setPort(testPort);
        outstationDataLinkLayer.addDataLinkLayerListener(outstationDataLinkInterceptor);

        outstationDataLinkLayer.close();
    }

    private void tryCloneMessageProperties(MessageProperties messageProperties, MessageProperties responseProperties) {
        try {
            BeanUtils.copyProperties(responseProperties, messageProperties);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
