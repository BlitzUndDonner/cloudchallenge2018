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
        when(context.element()).thenReturn("{\n" +
                "  \"flight-number\": \"F-45\",\n" +
                "  \"airport\": \"N21CM\",\n" +
                "  \"message\": \"hello world alpha bravo charlie delta\",\n" +
                "  \"timestamp\": \"2018-11-19T07:32:02:123Z\"\n" +
                "}");

        DataExtractor testee = new DataExtractor();
        testee.processElement(context);

        ArgumentCaptor<FlightMessageDto> argument = ArgumentCaptor.forClass(FlightMessageDto.class);
        verify(context).output(argument.capture());

        assertEquals("F-45", argument.getValue().getFlightNumber());
    }
}