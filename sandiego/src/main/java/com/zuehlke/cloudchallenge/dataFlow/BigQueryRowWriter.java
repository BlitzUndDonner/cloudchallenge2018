package com.zuehlke.cloudchallenge.dataFlow;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.zuehlke.cloudchallenge.FlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.ArrayList;
import java.util.List;

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
