package com.zuehlke.cloudchallenge.dataFlow;

import com.google.api.services.bigquery.model.TableRow;
import org.apache.beam.sdk.transforms.DoFn;

public class BigQueryRowWriter extends DoFn<FlightMessageDto, TableRow> {

    @ProcessElement
    public void processElement(ProcessContext c) {
        FlightMessageDto message = c.element();

        TableRow row = new TableRow();

        row.set("timestamp", message.getTimestamp());
        row.set("flight_number", message.getFlightNumber());
        row.set("airport", message.getAirport());
        row.set("message", message.getMessage());

        c.output(row);
    }
}
