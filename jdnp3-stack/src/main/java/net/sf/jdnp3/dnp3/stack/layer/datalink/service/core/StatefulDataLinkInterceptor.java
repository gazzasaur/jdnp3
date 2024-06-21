package net.sf.jdnp3.dnp3.stack.layer.datalink.service.concurrent.tcp.server;

import static java.util.Arrays.asList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkInterceptor;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkLayer;
import net.sf.jdnp3.dnp3.stack.layer.datalink.service.core.DataLinkListener;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

// TODO For master stations this should also be a datalink layer to store state from outstations
public class StatefulDataLinkInterceptor implements DataLinkInterceptor {
    private static final List<FunctionCode> ACKED_FUNCTION_CODES = asList(
        FunctionCode.TEST_LINK_STATES,
        FunctionCode.RESET_LINK_STATUS,
        FunctionCode.CONFIRMED_USER_DATA,
        FunctionCode.REQUEST_LINK_STATUS
    );
    
    private long destinationAddress;
    private DataLinkLayer dataLinkLayer;
    private DataLinkListener dataLinkListener;

    // Inbound link state tracking (secondary)
    private boolean master;
    private Map<Integer, Boolean> expectedFrameCheckBitState = new HashMap<>();

    public StatefulDataLinkInterceptor(boolean master, DataLinkLayer dataLinkLayer, long destinationAddress, DataLinkListener dataLinkListener) {
        this.master = master;
        this.dataLinkLayer = dataLinkLayer;
        this.dataLinkListener = dataLinkListener;
        this.destinationAddress = destinationAddress;
    }

    public void receiveData(MessageProperties messageProperties, DataLinkFrame frame) {
        if (messageProperties.getDestinationAddress() != destinationAddress) {
            return;
        }
        if (messageProperties.isMaster() && this.master) {
            return;
        }
        if (frame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.UNCONFIRMED_USER_DATA) {
            dataLinkListener.receiveData(messageProperties, frame.getData());
        }

        DataLinkFrame responseFrame = new DataLinkFrame();
        DataLinkFrameHeader dataLinkFrameHeader = responseFrame.getDataLinkFrameHeader();
        dataLinkFrameHeader.setDestination(frame.getDataLinkFrameHeader().getSource());
        dataLinkFrameHeader.setSource(frame.getDataLinkFrameHeader().getDestination());
        dataLinkFrameHeader.setDirection(master ? Direction.MASTER_TO_OUTSTATION : Direction.OUTSTATION_TO_MASTER);

        if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.RESET_LINK_STATUS) {
            expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), true);
            dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
            dataLinkLayer.sendData(messageProperties, frame);
            return;
        } else if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.TEST_LINK_STATES) {
            Boolean expectedFcb = expectedFrameCheckBitState.get(messageProperties.getSourceAddress());
            if (expectedFcb != null && expectedFcb.booleanValue() == frame.getDataLinkFrameHeader().isFcb()) {
                expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), !expectedFcb.booleanValue());
                dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
            } else {
                dataLinkFrameHeader.setFunctionCode(FunctionCode.NACK);
            }
            dataLinkLayer.sendData(messageProperties, frame);
            return;
        } else if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().isFcvDfc()) {
            Boolean expectedFcb = expectedFrameCheckBitState.get(messageProperties.getSourceAddress());
            if (expectedFcb != null && expectedFcb.booleanValue() == frame.getDataLinkFrameHeader().isFcb()) {
                expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), !expectedFcb.booleanValue());
                dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
            } else {
                dataLinkFrameHeader.setFunctionCode(FunctionCode.NACK);
            }
            dataLinkLayer.sendData(messageProperties, frame);
        }
        dataLinkListener.receiveData(messageProperties, frame.getData());
    }    
}
