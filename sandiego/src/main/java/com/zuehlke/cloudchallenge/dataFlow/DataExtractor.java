package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.Base64;

public class DataExtractor extends DoFn<String, FlightMessageDto> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        String raw = c.element();
        System.out.println(raw);
       // String line = new String(Base64.getDecoder().decode(raw));
        try {
            c.output(FlightMessageDto.of(raw));
        } catch (IllegalMessageException e) {
            e.printStackTrace();
        }
    }
}
