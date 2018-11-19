package com.zuehlke.cloudchallenge;

public class FlightMessageDto {
    private String flightNumber;
    private String airport;
    private String message;
    private String timestamp;
    private String messageWordCount;

    public FlightMessageDto() {
    }

    public static FlightMessageDto of(String line) throws IllegalMessageException {
        return new FlightMessageDto();
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
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessageWordCount() {
        return messageWordCount;
    }

    public void setMessageWordCount(String messageWordCount) {
        this.messageWordCount = messageWordCount;
    }
}
