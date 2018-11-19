package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.FlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DataExtractorTest {

    @Captor
    ArgumentCaptor<Object> captor;

    @Test
    public void processElement() {

        DataExtractor testee = new DataExtractor();

        ProcessContext processContext = mock(ProcessContext.class);
        when(processContext.element()).thenReturn("ewogICJmbGlnaHQtbnVtYmVyIjogIkYtNDUiLAogICJhaXJwb3J0IjogIk4yMUNNIiwKICAibWVzc2FnZSI6ICJoZWxsbyB3b3JsZCBhbHBoYSBicmF2byBjaGFybGllIGRlbHRhIiwKICAidGltZXN0YW1wIjogIjIwMTgtMTEtMTlUMDc6MzI6MDI6MTIzWiIKfQ==");

        testee.processElement(processContext);

        // verify(processContext).output(any(FlightMessageDto.class));

        // assertEquals("F-45", captor.getValue().getFlightNumber());
    }
}