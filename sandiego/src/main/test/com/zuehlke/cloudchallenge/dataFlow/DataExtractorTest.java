package com.zuehlke.cloudchallenge.dataFlow;

import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataExtractorTest {

    @Test
    public void processElement() {
        ProcessContext context = mock(ProcessContext.class);
        when(context.element()).thenReturn("ewogICJmbGlnaHQtbnVtYmVyIjogIkYtNDUiLAogICJhaXJwb3J0IjogIk4yMUNNIiwKICAibWVzc2FnZSI6ICJoZWxsbyB3b3JsZCBhbHBoYSBicmF2byBjaGFybGllIGRlbHRhIiwKICAidGltZXN0YW1wIjogIjIwMTgtMTEtMTlUMDc6MzI6MDI6MTIzWiIKfQ==");

        DataExtractor testee = new DataExtractor();
        testee.processElement(context);

        ArgumentCaptor<FlightMessageDto> argument = ArgumentCaptor.forClass(FlightMessageDto.class);
        verify(context).output(argument.capture());

        assertEquals("F-45", argument.getValue().getFlightNumber());
    }
}