package com.zuehlke.cloudchallenge;

import com.zuehlke.cloudchallenge.dataFlow.ProcessedFlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

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

