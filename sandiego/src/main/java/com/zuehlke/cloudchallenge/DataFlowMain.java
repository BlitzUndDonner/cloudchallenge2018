package com.zuehlke.cloudchallenge;

import com.google.api.services.bigquery.model.TableFieldSchema;
import com.google.api.services.bigquery.model.TableSchema;
import com.zuehlke.cloudchallenge.dataFlow.*;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.bigquery.BigQueryIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.PCollectionTuple;
import org.apache.beam.sdk.values.TupleTag;
import org.apache.beam.sdk.values.TupleTagList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DataFlowMain {
    private static final Logger LOG = LoggerFactory.getLogger(DataFlowMain.class);

    public interface DataFlowOptions extends DataflowPipelineOptions {
        @Description("the topic to consume messages from")
        String getRequestTopic();

        void setRequestTopic(String requestTopic);

        @Description("the topic to push messages to")
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

        TupleTag<FlightMessageDto> successTag = new TupleTag<FlightMessageDto>() {
        };
        TupleTag<FailureDto> deadLetterTag = new TupleTag<FailureDto>() {
        };

        PCollectionTuple outputTuple = p
                .apply("GetMessages", PubsubIO.readStrings().fromTopic(requestTopic))
                .apply("ExtractDataFailSafe", doFailSafeExtracting(successTag, deadLetterTag));

        outputTuple.get(deadLetterTag)
                .apply("WriteError", ParDo.of(new DoFn<FailureDto, Void>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        LOG.error("element in dead letter queue: " + c.element());
                    }
                }));

        PCollection<FlightMessageDto> currentFlightMessages = outputTuple.get(successTag);

        currentFlightMessages
                .apply("AddWordCount", ParDo.of(new WordCount()))
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

        p.run();
    }

    private static ParDo.MultiOutput<String, FlightMessageDto> doFailSafeExtracting(TupleTag<FlightMessageDto> successTag, TupleTag<FailureDto> deadLetterTag) {
        return ParDo.of(new DoFn<String, FlightMessageDto>() {
            @ProcessElement
            public void processElement(ProcessContext c) {
                try {
                    String raw = c.element();
                    LOG.info(raw);
                    c.output(FlightMessageDto.of(raw));
                } catch (IllegalMessageException e) {
                    LOG.error("failed to deserialize message", e);
                    c.output(deadLetterTag, new FailureDto(e.getMessage()));
                }
            }
        }).withOutputTags(successTag, TupleTagList.of(deadLetterTag));
    }

}
