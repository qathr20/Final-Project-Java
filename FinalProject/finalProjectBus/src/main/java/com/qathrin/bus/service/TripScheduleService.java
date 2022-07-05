package com.qathrin.bus.service;

import com.qathrin.bus.model.Trip;
import com.qathrin.bus.model.TripSchedule;
import com.qathrin.bus.payload.request.TripScheduleRequest;
import com.qathrin.bus.repository.TripRepository;
import com.qathrin.bus.repository.TripScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class TripScheduleService {
    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    TripRepository tripRepository;

    public List<TripSchedule> ListAllTripSchedule() {
        return tripScheduleRepository.findAll();
    }

    public void saveTripSchedule(TripSchedule tripSchedule) {
        tripScheduleRepository.save(tripSchedule);
    }

    public TripSchedule getTripSchedule(Integer id) {
        return tripScheduleRepository.findById(Long.valueOf(id)).get();
    }

    public void deleteTrip(Integer id) {
        tripScheduleRepository.deleteById(Long.valueOf(id));
    }


    public String checkIfDateIsGreaterThanToday(String requestedDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date myDate = sdf.parse(requestedDate);
        Date today = Calendar.getInstance().getTime();
        // check if requested date is less than today
        if (myDate.before(today)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Tanggal input harus lebih besar dari " + today.toString());
        } else {
            return myDate.toString();
        }
    }

    public TripSchedule addNewTrip(TripScheduleRequest tripScheduleRequest) throws ParseException {
        Optional<Trip> trip = tripRepository.findById((long) tripScheduleRequest.getTripDetail());
        if (!trip.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip with Id " + trip.get().getId() + "not found");
        }
        String requestDate = tripScheduleRequest.getTripDate();
        String checkedDate = checkIfDateIsGreaterThanToday(requestDate);
        try {
            TripSchedule tripSchedule = new TripSchedule(
                    checkedDate,
                    tripScheduleRequest.getAvailableSeats(),
                    trip.get());
            TripSchedule newTrip = tripScheduleRepository.save(tripSchedule);
            return newTrip;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
    }

    public TripSchedule updatingTrip(Long id, TripScheduleRequest tripScheduleRequest) throws ParseException {
        Optional<Trip> trip = tripRepository.findById((long) tripScheduleRequest.getTripDetail());
        Optional<TripSchedule> tripSchedule = tripScheduleRepository.findById(id);
        if (!tripSchedule.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Tripschedule with Id " + tripSchedule.get().getId() + "not found");
        }
        if (!trip.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Trip with Id " + trip.get().getId() + "not found");
        }
        String requestDate = tripScheduleRequest.getTripDate();
        String checkedDate = checkIfDateIsGreaterThanToday(requestDate);
        try {
            TripSchedule updatedTrip = new TripSchedule(
                    checkedDate,
                    tripScheduleRequest.getAvailableSeats(),
                    trip.get());
            TripSchedule savedTrip = tripScheduleRepository.save(updatedTrip);
            return savedTrip;
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e.getCause());
        }
    }
}
