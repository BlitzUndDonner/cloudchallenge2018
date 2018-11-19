package com.zuehlke.cloudchallenge.dataFlow;

import org.apache.beam.sdk.transforms.DoFn;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class ProcessedFlightMessageSerializer extends DoFn<ProcessedFlightMessageDto, String> {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessedFlightMessageSerializer.class);

    @ProcessElement
    public void processElement(ProcessContext c) {
        ProcessedFlightMessageDto raw = c.element();
        ObjectMapper mapper = new ObjectMapper();
        try {
            c.output(mapper.writeValueAsString(raw.getFlightMessageDto()));
        } catch (IOException e) {
            LOG.error("failed to serialize message dto", e);
        }
    }
}
