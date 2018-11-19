package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.FlightMessageDto;
import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.Base64;

public class DataExtractor extends DoFn<String, FlightMessageDto> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        String raw = c.element();
        String line = new String(Base64.getDecoder().decode(raw));
        System.out.println(line);
        try {
            c.output(FlightMessageDto.of(line));
        } catch (IllegalMessageException e) {
            e.printStackTrace();
        }
    }
}
