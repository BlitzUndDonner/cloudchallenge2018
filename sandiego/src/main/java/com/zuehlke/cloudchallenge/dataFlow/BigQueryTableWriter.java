package com.zuehlke.cloudchallenge.dataFlow;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableRow;
import com.google.api.services.bigquery.model.TableSchema;
import com.zuehlke.cloudchallenge.FlightMessageDto;
import org.apache.beam.sdk.transforms.DoFn;

import java.util.ArrayList;
import java.util.List;

public class BigQueryTableWriter extends DoFn<FlightMessageDto, TableRow> {

    private final TableSchema schema;

    public BigQueryTableWriter() {
        List<TableFieldSchema> fields = new ArrayList<>();
        fields.add(new TableFieldSchema().setName("timestamp").setType("TIMESTAMP"));
        fields.add(new TableFieldSchema().setName("flight_number").setType("STRING"));
        fields.add(new TableFieldSchema().setName("airport").setType("STRING"));
        fields.add(new TableFieldSchema().setName("message").setType("STRING"));

        schema = new TableSchema().setFields(fields);
    }

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
