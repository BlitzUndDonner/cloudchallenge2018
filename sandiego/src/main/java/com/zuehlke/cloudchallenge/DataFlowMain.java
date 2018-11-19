package com.zuehlke.cloudchallenge;

import com.zuehlke.cloudchallenge.dataFlow.BigQueryTableWriter;
import com.zuehlke.cloudchallenge.dataFlow.DataExtractor;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;

import java.util.Collections;

public class DataFlowMain {

    public interface DataFlowOptions extends DataflowPipelineOptions {
/*
@Description("the topic to consume messages from")
        @Default.String("request-t1-europe-north1")
        String getTopic();
        */
    }

    public static void main(String[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.println("argument " + i + " : " + args[i]);
        }

        DataFlowOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(DataFlowOptions.class);
        options.setStreaming(true);
        options.setFilesToStage(Collections.emptyList());
        Pipeline p = Pipeline.create(options);

        String topic = "projects/" + options.getProject() + "/topics/" + "request-t1-europe-north1";
        System.out.println(topic);

        PCollection<FlightMessageDto> currentFlightMessages = p
                .apply("GetMessages", PubsubIO.readStrings().fromTopic(topic))
                .apply("ExtractData", ParDo.of(new DataExtractor()));

        currentFlightMessages
                .apply("WriteToBigQueryTable", ParDo.of(new BigQueryTableWriter()));
    }
}
