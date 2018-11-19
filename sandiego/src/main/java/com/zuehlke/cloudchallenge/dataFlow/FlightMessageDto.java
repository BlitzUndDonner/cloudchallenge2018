package com.zuehlke.cloudchallenge.dataFlow;

import com.zuehlke.cloudchallenge.IllegalMessageException;
import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.Serializable;

@DefaultCoder(AvroCoder.class)
public class FlightMessageDto implements Serializable {
    @JsonProperty("flight-number")
    private String flightNumber;

    private String airport;
    private String message;
    private String timestamp;

    @JsonProperty("message-word-count")
    private int messageWordCount;

    public FlightMessageDto() {
    }

    public static FlightMessageDto of(String line) throws IllegalMessageException {
        try {
            return new ObjectMapper().readValue(line, FlightMessageDto.class);
        } catch (IOException e) {
            throw new IllegalMessageException(e);
        }
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirport() {
        return airport;
    }

    public void setAirport(String airport) {
        this.airport = airport;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
        //int length = message.split(" ").length;
        //setMessageWordCount(length);
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public int getMessageWordCount() {
        return messageWordCount;
    }

    public void setMessageWordCount(int messageWordCount) {
        this.messageWordCount = messageWordCount;
    }
}
