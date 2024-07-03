package net.sf.jdnp3.dnp3.stack.layer.datalink.inttest;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.mutable.MutableObject;
import org.junit.Ignore;
import org.junit.Test;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.client.TcpClientDataLinkService;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server.TcpServerDataLinkService;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.StatefulDataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;
import net.sf.jdnp3.dnp3.stack.nio.DataPump;

public class DataLinkOutstationTest {
    @Test
    public void testUnconfirmedUserData() throws Exception {
        var testPort = RandomUtils.nextInt(40000, 60000);

        var outstationDataLinkLayer = new TcpServerDataLinkService();
        var outstationDataLinkInterceptor = new StatefulDataLinkInterceptor(false, outstationDataLinkLayer, 1234, (MessageProperties messageProperties, List<Byte> data) -> {
            // Echo the data back.
            var reply = new ArrayList<>(Arrays.asList(ArrayUtils.toObject("Outstation: ".getBytes())));
            reply.addAll(data);

            var responseProperties = new MessageProperties();
            responseProperties.setChannelId(messageProperties.getChannelId());
            responseProperties.setMaster(false);
            responseProperties.setPrimary(true);
            responseProperties.setDestinationAddress(messageProperties.getSourceAddress());
            responseProperties.setSourceAddress(messageProperties.getDestinationAddress());
            outstationDataLinkLayer.sendData(responseProperties, reply);
        });
        outstationDataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        outstationDataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        outstationDataLinkLayer.setHost("localhost");
        outstationDataLinkLayer.setPort(testPort);
        outstationDataLinkLayer.addDataLinkLayerListener(outstationDataLinkInterceptor);
        outstationDataLinkLayer.start();

        var echoedResponse = new MutableObject<String>();
        var masterStationLataLinkLayer = new TcpClientDataLinkService("localhost", testPort);
        var masterStationDataLinkInterceptor = new StatefulDataLinkInterceptor(true, masterStationLataLinkLayer, 200, (messageProperties, data) -> {
            synchronized (echoedResponse) {
                echoedResponse.setValue(new String(ArrayUtils.toPrimitive(data.toArray(new Byte[0]))));
            }
        });
        masterStationLataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        masterStationLataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        masterStationLataLinkLayer.addDataLinkLayerListener(masterStationDataLinkInterceptor);
        masterStationLataLinkLayer.start();

        Thread.sleep(100);

        masterStationLataLinkLayer.sendData(new MessageProperties() {{
            setChannelId(masterStationLataLinkLayer.getChannelId());
            setDestinationAddress(1234);
            setMaster(true);
            setPrimary(true);
            setSourceAddress(200);
        }}, Arrays.asList(ArrayUtils.toObject("Hi".getBytes())));

        Thread.sleep(100);

        assertThat(echoedResponse.getValue(), is("Outstation: Hi"));

        outstationDataLinkLayer.close();
        masterStationLataLinkLayer.close();
    }

    @Test
    public void testConfirmedUserData() throws Exception {
        var testPort = RandomUtils.nextInt(40000, 60000);

        var outstationDataLinkLayer = new TcpServerDataLinkService();
        var outstationDataLinkInterceptor = new StatefulDataLinkInterceptor(false, outstationDataLinkLayer, 1234, (MessageProperties messageProperties, List<Byte> data) -> {
            // Echo the data back.
            var reply = new ArrayList<>(Arrays.asList(ArrayUtils.toObject("Outstation: ".getBytes())));
            reply.addAll(data);

            var responseProperties = new MessageProperties();
            responseProperties.setChannelId(messageProperties.getChannelId());
            responseProperties.setMaster(false);
            responseProperties.setPrimary(true);
            responseProperties.setDestinationAddress(messageProperties.getSourceAddress());
            responseProperties.setSourceAddress(messageProperties.getDestinationAddress());
            outstationDataLinkLayer.sendData(responseProperties, reply);
        });
        outstationDataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        outstationDataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        outstationDataLinkLayer.setHost("localhost");
        outstationDataLinkLayer.setPort(testPort);
        outstationDataLinkLayer.addDataLinkLayerListener(outstationDataLinkInterceptor);
        outstationDataLinkLayer.start();

        var echoedResponse = new MutableObject<String>("Nope");
        var masterStationLataLinkLayer = new TcpClientDataLinkService("localhost", testPort);
        var masterStationDataLinkInterceptor = new StatefulDataLinkInterceptor(true, masterStationLataLinkLayer, 200, (messageProperties, data) -> {
            synchronized (echoedResponse) {
                echoedResponse.setValue(new String(ArrayUtils.toPrimitive(data.toArray(new Byte[0]))));
            }
        });
        masterStationLataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        masterStationLataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        masterStationLataLinkLayer.addDataLinkLayerListener(masterStationDataLinkInterceptor);
        masterStationLataLinkLayer.start();

        Thread.sleep(100);

        var messageProperties = new MessageProperties() {{
            setChannelId(masterStationLataLinkLayer.getChannelId());
            setDestinationAddress(1234);
            setMaster(true);
            setPrimary(true);
            setSourceAddress(200);
        }};
        var resetDataLinkFrame = new DataLinkFrame() {{
            getDataLinkFrameHeader().setDestination(messageProperties.getDestinationAddress());
            getDataLinkFrameHeader().setSource(messageProperties.getSourceAddress());
            getDataLinkFrameHeader().setDirection(MASTER_TO_OUTSTATION);
            getDataLinkFrameHeader().setFcb(false);
            getDataLinkFrameHeader().setFcvDfc(false);
            getDataLinkFrameHeader().setFunctionCode(FunctionCode.RESET_LINK_STATUS);
            getDataLinkFrameHeader().setPrimary(true);
            setData(Arrays.asList(ArrayUtils.toObject("Hi".getBytes())));
        }};
        masterStationLataLinkLayer.sendData(messageProperties, resetDataLinkFrame);

        var dataLinkFrame = new DataLinkFrame() {{
            getDataLinkFrameHeader().setDestination(messageProperties.getDestinationAddress());
            getDataLinkFrameHeader().setSource(messageProperties.getSourceAddress());
            getDataLinkFrameHeader().setDirection(MASTER_TO_OUTSTATION);
            getDataLinkFrameHeader().setFcb(true);
            getDataLinkFrameHeader().setFcvDfc(true);
            getDataLinkFrameHeader().setFunctionCode(FunctionCode.CONFIRMED_USER_DATA);
            getDataLinkFrameHeader().setPrimary(true);
            setData(Arrays.asList(ArrayUtils.toObject("Hi".getBytes())));
        }};
        masterStationLataLinkLayer.sendData(messageProperties, dataLinkFrame);

        Thread.sleep(100);

        assertThat(echoedResponse.getValue(), is("Outstation: Hi"));

        dataLinkFrame.setData(Arrays.asList(ArrayUtils.toObject("Yo".getBytes())));
        dataLinkFrame.getDataLinkFrameHeader().setFcb(false);
        masterStationLataLinkLayer.sendData(messageProperties, dataLinkFrame);

        Thread.sleep(100);

        assertThat(echoedResponse.getValue(), is("Outstation: Yo"));

        outstationDataLinkLayer.close();
        masterStationLataLinkLayer.close();
    }

    @Test
    public void testConfirmedUserDataBeforeReset() throws Exception {
        var testPort = RandomUtils.nextInt(40000, 60000);

        var outstationDataLinkLayer = new TcpServerDataLinkService();
        var outstationDataLinkInterceptor = new StatefulDataLinkInterceptor(false, outstationDataLinkLayer, 1234, (MessageProperties messageProperties, List<Byte> data) -> {
            // Echo the data back.
            var reply = new ArrayList<>(Arrays.asList(ArrayUtils.toObject("Outstation: ".getBytes())));
            reply.addAll(data);

            var responseProperties = new MessageProperties();
            responseProperties.setChannelId(messageProperties.getChannelId());
            responseProperties.setMaster(false);
            responseProperties.setPrimary(true);
            responseProperties.setDestinationAddress(messageProperties.getSourceAddress());
            responseProperties.setSourceAddress(messageProperties.getDestinationAddress());
            outstationDataLinkLayer.sendData(responseProperties, reply);
        });
        outstationDataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        outstationDataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        outstationDataLinkLayer.setHost("localhost");
        outstationDataLinkLayer.setPort(testPort);
        outstationDataLinkLayer.addDataLinkLayerListener(outstationDataLinkInterceptor);
        outstationDataLinkLayer.start();

        var echoedResponse = new MutableObject<String>("Nope");
        var masterStationLataLinkLayer = new TcpClientDataLinkService("localhost", testPort);
        var masterStationDataLinkInterceptor = new StatefulDataLinkInterceptor(true, masterStationLataLinkLayer, 200, (messageProperties, data) -> {
            synchronized (echoedResponse) {
                echoedResponse.setValue(new String(ArrayUtils.toPrimitive(data.toArray(new Byte[0]))));
            }
        });
        masterStationLataLinkLayer.setExecutorService(Executors.newFixedThreadPool(2));
        masterStationLataLinkLayer.setDataPump(new DataPump() {{
            start();
        }});
        masterStationLataLinkLayer.addDataLinkLayerListener(masterStationDataLinkInterceptor);
        masterStationLataLinkLayer.start();

        Thread.sleep(100);

        var messageProperties = new MessageProperties() {{
            setChannelId(masterStationLataLinkLayer.getChannelId());
            setDestinationAddress(1234);
            setMaster(true);
            setPrimary(true);
            setSourceAddress(200);
        }};
        var dataLinkFrame = new DataLinkFrame() {{
            getDataLinkFrameHeader().setDestination(messageProperties.getDestinationAddress());
            getDataLinkFrameHeader().setSource(messageProperties.getSourceAddress());
            getDataLinkFrameHeader().setDirection(MASTER_TO_OUTSTATION);
            getDataLinkFrameHeader().setFcb(true);
            getDataLinkFrameHeader().setFcvDfc(true);
            getDataLinkFrameHeader().setFunctionCode(FunctionCode.CONFIRMED_USER_DATA);
            getDataLinkFrameHeader().setPrimary(true);
            setData(Arrays.asList(ArrayUtils.toObject("Hi".getBytes())));
        }};
        masterStationLataLinkLayer.sendData(messageProperties, dataLinkFrame);

        Thread.sleep(100);

        assertThat(echoedResponse.getValue(), is("Nope"));

        outstationDataLinkLayer.close();
        masterStationLataLinkLayer.close();
    }
}
