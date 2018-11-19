package com.zuehlke.cloudchallenge.dataFlow;

import com.google.api.services.bigquery.model.TableRow;
import com.zuehlke.cloudchallenge.FlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn;

public class BigQueryTableWriter extends DoFn<FlightMessageDto, TableRow> {
    @ProcessElement
    public void processElement(ProcessContext c) {
        FlightMessageDto message = c.element();

        TableRow row = new TableRow();

        c.output(row);
    }
}
