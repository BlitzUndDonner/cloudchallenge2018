package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.DataFlowMain;
import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.transforms.DoFn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class DataExtractor extends DoFn<String, FlightMessageDto> {

    private static final Logger LOG = LoggerFactory.getLogger(DataExtractor.class);

    @ProcessElement
    public void processElement(ProcessContext c) {
        String raw = c.element();
        System.out.println(raw);
       // String line = new String(Base64.getDecoder().decode(raw));
        try {
            c.output(FlightMessageDto.of(raw));
        } catch (IllegalMessageException e) {
            LOG.error("failed to deserialize message", e);
        }
    }
}
