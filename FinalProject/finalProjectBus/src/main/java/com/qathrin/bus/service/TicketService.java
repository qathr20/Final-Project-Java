package com.qathrin.bus.service;

import com.qathrin.bus.model.Ticket;
import com.qathrin.bus.model.Trip;
import com.qathrin.bus.model.TripSchedule;
import com.qathrin.bus.model.User;
import com.qathrin.bus.payload.request.TicketRequest;
import com.qathrin.bus.repository.TicketRepository;
import com.qathrin.bus.repository.TripRepository;
import com.qathrin.bus.repository.TripScheduleRepository;
import com.qathrin.bus.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    public List<Ticket> ListAllTicket() {

        return ticketRepository.findAll();
    }

    public void saveTicket(Ticket ticket) {

        ticketRepository.save(ticket);
    }

    public Ticket getTicket(Integer id) {

        return ticketRepository.findById(Long.valueOf(id)).get();
    }

    public void deletTicket(Integer id) {

        ticketRepository.deleteById(Long.valueOf(id));
    }

    public Optional<User> checkIfUserPresent(String currentUser) {

        // get user from database
        Optional<User> user = userRepository.findByUsername(currentUser);

        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
        }
        return user;
    }

    public Optional<TripSchedule> checkIfTripScheduleAvailable(TicketRequest ticketRequest) throws ParseException {

        // find trip schedule by id
        Optional<TripSchedule> tripSchedule = tripScheduleRepository.findById(ticketRequest.getTripScheduleId());

        String journeyDate = ticketRequest.getJourneyDate();
        String requestedDate = tripSchedule.get().getTripDate();

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date myDate = sdf.parse(requestedDate);
        Date tripDate = sdf.parse(journeyDate);

        if (!tripSchedule.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip schedule not found");
        }
        if (!myDate.equals(tripDate)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No trip found at date " + journeyDate);
        }
        if (tripSchedule.get().getAvailableSeats() == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticked sold out");
        }
        return tripSchedule;
    }

    public Ticket bookingTicket(TicketRequest ticketRequest) throws ParseException {

        // get logged in user
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String currentUser = auth.getName();

        Optional<User> user = checkIfUserPresent(currentUser);
        Optional<TripSchedule> tripSchedule = checkIfTripScheduleAvailable(ticketRequest);

        try {
            Ticket ticket = new Ticket();
            // kursi passenger dimulai dari descending 30, 29, 28, .... 1
            ticket.setSeatNumber(tripSchedule.get().getTripDetail().getBus().getCapacity() - tripSchedule.get().getAvailableSeats());
            ticket.setCancellable(false);
            ticket.setJourneyDate(ticketRequest.getJourneyDate());
            ticket.setPassenger(user.get());
            ticket.setTripSchedule(tripSchedule.get());

            ticketRepository.save(ticket);

            // setiap (1) tiket yang dibeli akan mengurangi kursi sebanyak (1)
            tripSchedule.get().setAvailableSeats(tripSchedule.get().getAvailableSeats() - 1);

            // update trip schedule
            tripScheduleRepository.save(tripSchedule.get());

            return ticket;

        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
    }


    public Ticket updatingTicket(Long id, TicketRequest ticketRequest) throws ParseException {

        Optional<Ticket> ticket = ticketRepository.findById(id);

        if (!ticket.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ticket not found");
        }

        Optional<TripSchedule> tripSchedule = checkIfTripScheduleAvailable(ticketRequest);

        ticket.get().setJourneyDate(ticketRequest.getJourneyDate());
        ticket.get().setTripSchedule(tripSchedule.get());

        Ticket savedTicket = ticketRepository.save(ticket.get());

        return savedTicket;
    }
}
