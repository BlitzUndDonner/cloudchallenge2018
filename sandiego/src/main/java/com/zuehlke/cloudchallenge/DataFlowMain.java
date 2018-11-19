package com.zuehlke.cloudchallenge;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import com.zuehlke.cloudchallenge.dataFlow.*;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataFlowMain {
    private static final Logger LOG = LoggerFactory.getLogger(DataFlowMain.class);

    public interface DataFlowOptions extends DataflowPipelineOptions {
        @Description("the topic to consume messages from")
        @Default.String("request-t1-europe-north1")
        String getRequestTopic();

        void setRequestTopic(String requestTopic);

        @Description("the topic to push messages to")
        @Default.String("response-t1-europe-north1")
        String getResponseTopic();

        void setResponseTopic(String responseTopic);
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            LOG.info("argument " + i + " : " + args[i]);
        }

        DataFlowOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(DataFlowOptions.class);
        PipelineOptionsFactory.register(DataFlowOptions.class);

        options.setStreaming(true);

        Pipeline p = Pipeline.create(options);

        String requestTopic = "projects/" + options.getProject() + "/topics/" + options.getRequestTopic();
        String responseTopic = "projects/" + options.getProject() + "/topics/" + options.getResponseTopic();

        LOG.info("RequestTopic: " + requestTopic);
        LOG.info("ResponseTopic: " + responseTopic);

        PCollection<FlightMessageDto> currentFlightMessages = p
                .apply("GetMessages", PubsubIO.readStrings().fromTopic(requestTopic))
                .apply("ExtractData", ParDo.of(new DataExtractor()));

        currentFlightMessages.apply("AddWordCount", ParDo.of(new WordCount()))
                .apply("MapDtoToString", ParDo.of(new ProcessedFlightMessageSerializer()))
                .apply("WriteToPubSub", PubsubIO.writeStrings().to(responseTopic));

        String table = options.getProject() + ":flight_messages.raw_flight_messages";
        List<TableFieldSchema> fields = new ArrayList<>();
        fields.add(new TableFieldSchema().setName("timestamp").setType("TIMESTAMP"));
        fields.add(new TableFieldSchema().setName("flight_number").setType("STRING"));
        fields.add(new TableFieldSchema().setName("airport").setType("STRING"));
        fields.add(new TableFieldSchema().setName("message").setType("STRING"));

        TableSchema schema = new TableSchema().setFields(fields);

        currentFlightMessages
                .apply("WriteBigQueryRow", ParDo.of(new BigQueryRowWriter()))
                .apply("WriteToBigQuery", BigQueryIO.writeTableRows().to(table)//
                        .withSchema(schema)//
                        .withWriteDisposition(BigQueryIO.Write.WriteDisposition.WRITE_APPEND)
                        .withCreateDisposition(BigQueryIO.Write.CreateDisposition.CREATE_IF_NEEDED));

        PipelineResult result = p.run();
        // result.waitUntilFinish();
    }

}
