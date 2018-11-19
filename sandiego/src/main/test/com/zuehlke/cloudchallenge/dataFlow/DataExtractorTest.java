package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.FlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn.ProcessContext;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class DataExtractorTest {

    @Captor
    ArgumentCaptor<FlightMessageDto> captor;

    @Test
    public void processElement() {

        DataExtractor testee = new DataExtractor();

        ProcessContext processContext = mock(ProcessContext.class);
        when(processContext.element()).thenReturn("{\n" +
                "  \"flight-number\": \"F-45\",\n" +
                "  \"airport\": \"N21CM\",\n" +
                "  \"message\": \"hello world alpha bravo charlie delta\",\n" +
                "  \"timestamp\": \"2018-11-19T07:32:02:123Z\"\n" +
                "}");

        testee.processElement(processContext);

        verify(processContext).output(captor.capture());

        // assertEquals("F-45", captor.getValue().getFlightNumber());
    }
}