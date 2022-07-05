package com.qathrin.bus.dto.mapper;

import com.qathrin.bus.dto.model.bus.TripScheduleDto;
import com.qathrin.bus.model.Trip;
import com.qathrin.bus.model.TripSchedule;

/**
 * Created by Arpit Khandelwal.
 */
public class TripScheduleMapper {
    public static TripScheduleDto toTripScheduleDto(TripSchedule tripSchedule) {
        Trip tripDetails = tripSchedule.getTripDetail();
        return new TripScheduleDto()
                .setId(String.valueOf(tripSchedule.getId()))
                .setTripId(String.valueOf(tripDetails.getId()))
                .setBusCode(tripDetails.getBus().getCode())
                .setAvailableSeats(tripSchedule.getAvailableSeats())
                .setFare(tripDetails.getFare())
                .setJourneyTime(tripDetails.getJourneyTime())
                .setSourceStop(tripDetails.getSourceStop().getName())
                .setDestinationStop(tripDetails.getDestStop().getName());
    }
}
