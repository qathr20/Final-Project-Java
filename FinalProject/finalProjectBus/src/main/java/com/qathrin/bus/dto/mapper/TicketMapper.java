package com.qathrin.bus.dto.mapper;

import com.qathrin.bus.dto.model.bus.TicketDto;
import com.qathrin.bus.model.Ticket;

/**
 * Created by Arpit Khandelwal.
 */
public class TicketMapper {
    public static TicketDto toTicketDto(Ticket ticket) {
        return new TicketDto()
                .setId(String.valueOf(ticket.getId()))
                .setBusCode(ticket.getTripSchedule().getTripDetail().getBus().getCode())
                .setSeatNumber(ticket.getSeatNumber())
                .setSourceStop(ticket.getTripSchedule().getTripDetail().getSourceStop().getName())
                .setDestinationStop(ticket.getTripSchedule().getTripDetail().getDestStop().getName())
                .setCancellable(false)
                .setJourneyDate(ticket.getJourneyDate())
                .setPassengerName(ticket.getPassenger().getFirstName() + "_" + ticket.getPassenger().getLastName())
                .setPassengerMobileNumber(ticket.getPassenger().getMobileNumber());
    }
}
