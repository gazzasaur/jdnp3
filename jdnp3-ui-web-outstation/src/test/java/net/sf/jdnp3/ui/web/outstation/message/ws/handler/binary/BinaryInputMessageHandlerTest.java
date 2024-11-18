package net.sf.jdnp3.ui.web.outstation.message.ws.handler.binary;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.samePropertyValuesAs;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import net.sf.jdnp3.ui.web.outstation.database.core.DatabaseManager;
import net.sf.jdnp3.ui.web.outstation.database.point.binary.BinaryInputDataPoint;
import net.sf.jdnp3.ui.web.outstation.main.OutstationDevice;
import net.sf.jdnp3.ui.web.outstation.message.ws.core.Messanger;
import net.sf.jdnp3.ui.web.outstation.message.ws.model.binary.BinaryInputMessage;

@RunWith(MockitoJUnitRunner.class)
public class BinaryInputMessageHandlerTest {
    @Mock private Messanger mockMessanger;
    @Mock private DatabaseManager mockDatabaseManager;
    @Mock private OutstationDevice mockOutstationDevice;
    @Captor private ArgumentCaptor<BinaryInputDataPoint> captor;

    @Test
    public void testHandleMessage() {
        BinaryInputDataPoint existingBinaryInputDataPoint = new BinaryInputDataPoint();
        existingBinaryInputDataPoint.setIndex(10);
        existingBinaryInputDataPoint.setName("Existing name");
        existingBinaryInputDataPoint.setTags(new HashMap<String, String>() {{
            put("Existing key", "Existing value");
        }});
        existingBinaryInputDataPoint.setActive(true);
        existingBinaryInputDataPoint.setChatterFilter(true);
        existingBinaryInputDataPoint.setOnline(false);

        BinaryInputMessage inputMessage = new BinaryInputMessage();
        inputMessage.setIndex(10);
        inputMessage.setName("New name");
        inputMessage.setTags(new HashMap<String, String>() {{
            put("Ney key", "Ney value");
        }});
        inputMessage.setChatterFilter(false);
        inputMessage.setOnline(true);

        BinaryInputDataPoint expectedBinaryInputDataPoint = new BinaryInputDataPoint();
        expectedBinaryInputDataPoint.setIndex(10);
        expectedBinaryInputDataPoint.setName("Existing name"); // Preserve the name.
        expectedBinaryInputDataPoint.setTags(new HashMap<String, String>() {{
            put("Existing key", "Existing value");
        }});
        expectedBinaryInputDataPoint.setActive(true);
        expectedBinaryInputDataPoint.setChatterFilter(false);
        expectedBinaryInputDataPoint.setOnline(true);

        when(mockOutstationDevice.getDatabaseManager()).thenReturn(mockDatabaseManager);
        when(mockDatabaseManager.getBinaryInputDataPoint(10)).thenReturn(existingBinaryInputDataPoint);

        BinaryInputMessageHandler subject = new BinaryInputMessageHandler();
        subject.processMessage(mockMessanger, mockOutstationDevice, inputMessage);
        verify(mockDatabaseManager).setBinaryInputDataPoint(captor.capture());

        assertThat(captor.getValue(), samePropertyValuesAs(expectedBinaryInputDataPoint));
    }
}
