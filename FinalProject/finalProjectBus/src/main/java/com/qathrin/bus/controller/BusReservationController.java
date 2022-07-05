package com.qathrin.bus.controller;

import com.qathrin.bus.controller.request.BookTicketRequest;
import com.qathrin.bus.controller.request.GetTripSchedulesRequest;
import com.qathrin.bus.dto.model.bus.TicketDto;
import com.qathrin.bus.dto.model.bus.TripDto;
import com.qathrin.bus.dto.model.bus.TripScheduleDto;
import com.qathrin.bus.dto.model.user.UserDto;
import com.qathrin.bus.dto.response.Response;
import com.qathrin.bus.model.User;
import com.qathrin.bus.service.BusReservationService;
import com.qathrin.bus.service.UserService;
import com.qathrin.bus.util.DateUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by Arpit Khandelwal.
 */
@RestController
@RequestMapping("/api/v1/reservation")
public class BusReservationController {
    @Autowired
    private BusReservationService busReservationService;

    @Autowired
    private UserService userService;

    @GetMapping("/stops")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getAllStops() {
        return Response
                .ok()
                .setPayload(busReservationService.getAllStops());
    }

    @GetMapping("/tripsbystops")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getTripsByStops(@RequestParam String destinationStop,
                                    @RequestParam String sourceStop) throws ParseException {
        List<TripDto> tripDtos = busReservationService.getAvailableTripsBetweenStops(
               destinationStop,
               sourceStop);
        if (!tripDtos.isEmpty()) {
            return Response.ok().setPayload(tripDtos);
        }
        return Response.notFound()
                .setErrors(String.format("No trips between source stop - '%s' and destination stop - '%s' are available at this time.", sourceStop, destinationStop));
    }

    @GetMapping("/tripschedules")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response getTripSchedules(@RequestParam String destinationStop,
                                     @RequestParam String sourceStop,
                                     @RequestParam String tripDate) throws ParseException {
        List<TripScheduleDto> tripScheduleDtos = busReservationService.getAvailableTripSchedules(
                sourceStop,
                destinationStop,
                DateUtils.formattedDate(new SimpleDateFormat("yyyy-MM-dd").parse(tripDate)));
        if (!tripScheduleDtos.isEmpty()) {
            return Response.ok().setPayload(tripScheduleDtos);
        }
        return Response.notFound()
                .setErrors(String.format("No trips between source stop - '%s' and destination stop - '%s' on date - '%s' are available at this time.", sourceStop, destinationStop, tripDate));
    }

    @PostMapping("/bookticket")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public Response bookTicket(@RequestBody @Valid BookTicketRequest bookTicketRequest) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails user = (UserDetails) auth.getPrincipal();
        Optional<UserDto> userDto = Optional.ofNullable(userService.findUserByUsername(user.getUsername()));
        if (userDto.isPresent()) {
            Optional<TripDto> tripDto = Optional
                    .ofNullable(busReservationService.getTripById(bookTicketRequest.getTripID()));
            if (tripDto.isPresent()) {
                Optional<TripScheduleDto> tripScheduleDto = Optional
                        .ofNullable(busReservationService.getTripSchedule(tripDto.get(), DateUtils.formattedDate(bookTicketRequest.getTripDate()), true));
                if (tripScheduleDto.isPresent()) {
                    Optional<TicketDto> ticketDto = Optional
                            .ofNullable(busReservationService.bookTicket(tripScheduleDto.get(), userDto.get()));
                    if (ticketDto.isPresent()) {
                        return Response.ok().setPayload(ticketDto.get());
                    }
                }
            }
        }
        return Response.badRequest().setErrors("Unable to process ticket booking.");
    }
}
