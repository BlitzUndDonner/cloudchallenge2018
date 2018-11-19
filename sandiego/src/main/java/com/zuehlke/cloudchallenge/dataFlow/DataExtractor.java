package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.FlightMessageDto;
import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.transforms.DoFn;

public class DataExtractor extends DoFn<String, FlightMessageDto> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        String line = c.element();
        System.out.println(line);
        try {
            c.output(FlightMessageDto.of(line));
        } catch (IllegalMessageException e) {
            e.printStackTrace();
        }
    }
}
