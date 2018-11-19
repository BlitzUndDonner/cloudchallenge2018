package com.zuehlke.cloudchallenge;

import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.options.Default;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

public class DataFlowMain {

    public interface DataFlowOptions extends DataflowPipelineOptions {
        @Description("the topic to consume messages from")
        @Default.String("request-t1-europe-north1")
        String getTopic();
    }

    public static void main(String[] args) {

        DataFlowOptions options = PipelineOptionsFactory.fromArgs(args).withValidation().as(DataFlowOptions.class);
        options.setStreaming(true);
        Pipeline p = Pipeline.create(options);

        String topic = "projects/" + options.getProject() + "/topics/" + options.getTopic();
        System.out.println(topic);

        p
                .apply("GetMessages", PubsubIO.readStrings().fromTopic(topic))
                .apply("ExtractData", ParDo.of(new DoFn<String, FlightMessageDto>() {
                    @ProcessElement
                    public void processElement(ProcessContext c) {
                        String line = c.element();
                        System.out.println(line);
                        try {
                            c.output(FlightMessageDto.of(line));
                        } catch (IllegalMessageException e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }
}
