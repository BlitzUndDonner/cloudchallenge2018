package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.dataFlow.FlightMessageDto;
import com.zuehlke.cloudchallenge.dataFlow.ProcessedFlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn;

/**
 * @author Lukas Hofmaier
 */
public class WordCount extends DoFn<FlightMessageDto, ProcessedFlightMessageDto> {

    @DoFn.ProcessElement
    public void processElement(ProcessContext c) throws Exception {
        FlightMessageDto flightMessageDto = c.element();

        c.output(ProcessedFlightMessageDto.newProcessedFlightMsg(flightMessageDto));
    }
}

