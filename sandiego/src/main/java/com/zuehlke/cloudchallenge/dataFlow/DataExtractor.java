package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.Base64;

public class DataExtractor extends DoFn<PubsubMessage, FlightMessageDto> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        PubsubMessage raw = c.element();
        String line = new String(Base64.getDecoder().decode(raw.getPayload()));
        System.out.println(line);
        try {
            c.output(FlightMessageDto.of(line));
        } catch (IllegalMessageException e) {
            e.printStackTrace();
        }
    }
}
