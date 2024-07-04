package net.sf.jdnp3.dnp3.stack.layer.datalink.service.core;

import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.MASTER_TO_OUTSTATION;
import static net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction.OUTSTATION_TO_MASTER;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrame;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.DataLinkFrameHeader;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.Direction;
import net.sf.jdnp3.dnp3.stack.layer.datalink.model.FunctionCode;
import net.sf.jdnp3.dnp3.stack.layer.datalink.util.DataLinkFrameUtils;
import net.sf.jdnp3.dnp3.stack.message.MessageProperties;

// TODO For master stations this should also be a datalink layer to store state from outstations
public class StatefulDataLinkInterceptor implements DataLinkInterceptor {
    private Logger logger = LoggerFactory.getLogger(StatefulDataLinkInterceptor.class);

    private long destinationAddress;
    private DataLinkLayer dataLinkLayer;
    private DataLinkListener dataLinkListener;

    // Inbound link state tracking (secondary)
    private boolean master;
    private Map<Integer, Boolean> expectedFrameCheckBitState = new ConcurrentHashMap<>();

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
            return;
        }

        DataLinkFrame responseFrame = new DataLinkFrame();
        DataLinkFrameHeader dataLinkFrameHeader = responseFrame.getDataLinkFrameHeader();
        dataLinkFrameHeader.setDestination(frame.getDataLinkFrameHeader().getSource());
        dataLinkFrameHeader.setSource(frame.getDataLinkFrameHeader().getDestination());
        dataLinkFrameHeader.setDirection(master ? Direction.MASTER_TO_OUTSTATION : Direction.OUTSTATION_TO_MASTER);
        dataLinkFrameHeader.setPrimary(false);

        if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.RESET_LINK_STATUS) {
            expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), true);
            dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
            dataLinkFrameHeader.setDirection(frame.getDataLinkFrameHeader().getDirection() == MASTER_TO_OUTSTATION ? MASTER_TO_OUTSTATION : OUTSTATION_TO_MASTER);
            dataLinkLayer.sendData(messageProperties, responseFrame);
            return;
        } else if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().isFcvDfc() && frame.getDataLinkFrameHeader().getFunctionCode() == FunctionCode.TEST_LINK_STATES) {
            Boolean expectedFcb = expectedFrameCheckBitState.get(messageProperties.getSourceAddress());
            if (expectedFcb != null && expectedFcb.booleanValue() == frame.getDataLinkFrameHeader().isFcb()) {
                expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), !expectedFcb.booleanValue());
                dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
            } else {
                dataLinkFrameHeader.setFunctionCode(FunctionCode.NACK);
            }
            dataLinkLayer.sendData(messageProperties, responseFrame);
            return;
        } else if (messageProperties.isPrimary() && frame.getDataLinkFrameHeader().isFcvDfc()) {
            Boolean expectedFcb = expectedFrameCheckBitState.get(messageProperties.getSourceAddress());
            if (expectedFcb != null && expectedFcb.booleanValue() == frame.getDataLinkFrameHeader().isFcb()) {
                expectedFrameCheckBitState.put(messageProperties.getSourceAddress(), !expectedFcb.booleanValue());
                dataLinkFrameHeader.setFunctionCode(FunctionCode.ACK);
                dataLinkListener.receiveData(messageProperties, frame.getData());
            } else {
                dataLinkFrameHeader.setFunctionCode(FunctionCode.NACK);
            }
            dataLinkLayer.sendData(messageProperties, responseFrame);
            return;
        }
        logger.error("Unknown payload type: " + frame.getDataLinkFrameHeader().getFunctionCode());
    }
}
