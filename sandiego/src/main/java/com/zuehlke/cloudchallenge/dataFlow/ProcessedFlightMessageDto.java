package com.zuehlke.cloudchallenge.dataFlow;

import org.apache.beam.sdk.coders.AvroCoder;
import org.apache.beam.sdk.coders.DefaultCoder;

/**
 * @author Lukas Hofmaier
 */
@DefaultCoder(AvroCoder.class)
public class ProcessedFlightMessageDto {
    private FlightMessageDto flightMessageDto;

    public ProcessedFlightMessageDto(FlightMessageDto flightMessageDto) {
        this.flightMessageDto = flightMessageDto;
    }

    public ProcessedFlightMessageDto() {
    }

    public static ProcessedFlightMessageDto newProcessedFlightMsg(FlightMessageDto flightMessageDto) {
        flightMessageDto.setMessageWordCount(flightMessageDto.getMessage().split(" ").length);
        return new ProcessedFlightMessageDto(flightMessageDto);
    }

    public FlightMessageDto getFlightMessageDto() {
        return flightMessageDto;
    }

    public void setFlightMessageDto(FlightMessageDto flightMessageDto) {
        this.flightMessageDto = flightMessageDto;
    }
}
