package com.qathrin.bus.dto.mapper;

import com.qathrin.bus.dto.model.bus.TripDto;
import com.qathrin.bus.model.Trip;

/**
 * Created by Arpit Khandelwal.
 */
public class TripMapper {
    public static TripDto toTripDto(Trip trip) {
        return new TripDto()
                .setId(String.valueOf(trip.getId()))
                .setAgencyCode(trip.getAgency().getCode())
                .setSourceStopCode(trip.getSourceStop().getCode())
                .setSourceStopName(trip.getSourceStop().getName())
                .setDestinationStopCode(trip.getDestStop().getCode())
                .setDestinationStopName(trip.getDestStop().getName())
                .setBusCode(trip.getBus().getCode())
                .setJourneyTime(trip.getJourneyTime())
                .setFare(trip.getFare());
    }
}
