package com.qathrin.bus.service;

import com.qathrin.bus.dto.mapper.TicketMapper;
import com.qathrin.bus.dto.mapper.TripMapper;
import com.qathrin.bus.dto.mapper.TripScheduleMapper;
import com.qathrin.bus.dto.model.bus.*;
import com.qathrin.bus.dto.model.user.UserDto;
import com.qathrin.bus.exception.BRSException;
import com.qathrin.bus.exception.EntityType;
import com.qathrin.bus.exception.ExceptionType;
import com.qathrin.bus.model.*;
import com.qathrin.bus.model.User;
import com.qathrin.bus.repository.*;
import com.qathrin.bus.util.RandomStringUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static com.qathrin.bus.exception.EntityType.*;
import static com.qathrin.bus.exception.ExceptionType.*;

/**
 * Created by Arpit Khandelwal.
 */
@Component
public class BusReservationService {
    @Autowired
    private AgencyRepository agencyRepository;

    @Autowired
    private BusRepository busRepository;

    @Autowired
    private StopRepository stopRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TripRepository tripRepository;

    @Autowired
    private TripScheduleRepository tripScheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ModelMapper modelMapper;


    public Set<StopDto> getAllStops() {
        return stopRepository.findAll()
                .stream()
                .map(stop -> modelMapper.map(stop, StopDto.class))
                .collect(Collectors.toCollection(TreeSet::new));
    }


    public StopDto getStopByCode(String stopCode) {
        Optional<Stop> stop = Optional.ofNullable(stopRepository.findByCode(stopCode));
        if (stop.isPresent()) {
            return modelMapper.map(stop.get(), StopDto.class);
        }
        throw exception(STOP, ENTITY_NOT_FOUND, stopCode);
    }


    public AgencyDto getAgency(UserDto userDto) {
        User user = getUser(userDto.getEmail());
        if (user != null) {
            Optional<Agency> agency = Optional.ofNullable(agencyRepository.findByOwner(user));
            if (agency.isPresent()) {
                return modelMapper.map(agency.get(), AgencyDto.class);
            }
            throw exceptionWithId(AGENCY, ENTITY_NOT_FOUND, "2", user.getEmail());
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }


    public AgencyDto addAgency(AgencyDto agencyDto) {
        User admin = getUser(agencyDto.getOwner().getEmail());
        if (admin != null) {
            Optional<Agency> agency = Optional.ofNullable(agencyRepository.findByName(agencyDto.getName()));
            if (!agency.isPresent()) {
                Agency agencyModel = new Agency();
                agencyModel.setName(agencyDto.getName());
                agencyModel.setDetails(agencyDto.getDetails());
                agencyModel.setCode(RandomStringUtil.getAlphaNumericString(8, agencyDto.getName()));
                agencyModel.setOwner(admin);
                agencyRepository.save(agencyModel);
                return modelMapper.map(agencyModel, AgencyDto.class);
            }
            throw exception(AGENCY, DUPLICATE_ENTITY, agencyDto.getName());
        }
        throw exception(USER, ENTITY_NOT_FOUND, agencyDto.getOwner().getEmail());
    }

    /**
     * Updates the agency with given Bus information
     *
     * @param agencyDto
     * @param busDto
     * @return
     */
    @Transactional
    public AgencyDto updateAgency(AgencyDto agencyDto, BusDto busDto) {
        Agency agency = getAgency(agencyDto.getCode());
        if (agency != null) {
            if (busDto != null) {
                Optional<Bus> bus = Optional.ofNullable(busRepository.findByCodeAndAgency(busDto.getCode(), agency));
                if (!bus.isPresent()) {
                    Bus busModel = new Bus();
                    busModel.setAgency(agency);
                    busModel.setCode(busDto.getCode());
                    busModel.setCapacity(busDto.getCapacity());
                    busModel.setMake(busDto.getMake());
                    busRepository.save(busModel);
                    if (agency.getBuses() == null) {
                        agency.setBuses(new HashSet<>());
                    }
                    agency.getBuses().add(busModel);
                    return modelMapper.map(agencyRepository.save(agency), AgencyDto.class);
                }
                throw exceptionWithId(BUS, DUPLICATE_ENTITY, "2", busDto.getCode(), agencyDto.getCode());
            } else {
                //update agency details case
                agency.setName(agencyDto.getName());
                agency.setDetails(agencyDto.getDetails());
                return modelMapper.map(agencyRepository.save(agency), AgencyDto.class);
            }
        }
        throw exceptionWithId(AGENCY, ENTITY_NOT_FOUND, "2", agencyDto.getOwner().getEmail());
    }


    public TripDto getTripById(String tripID) {
        Optional<Trip> trip = tripRepository.findById(Long.valueOf(tripID));
        if (trip.isPresent()) {
            return TripMapper.toTripDto(trip.get());
        }
        throw exception(TRIP, ENTITY_NOT_FOUND, tripID);
    }


    @Transactional
    public List<TripDto> addTrip(TripDto tripDto) {
        Stop sourceStop = getStop(tripDto.getSourceStopCode());
        if (sourceStop != null) {
            Stop destinationStop = getStop(tripDto.getDestinationStopCode());
            if (destinationStop != null) {
                if (!sourceStop.getCode().equalsIgnoreCase(destinationStop.getCode())) {
                    Agency agency = getAgency(tripDto.getAgencyCode());
                    if (agency != null) {
                        Bus bus = getBus(tripDto.getBusCode());
                        if (bus != null) {
                            //Each new trip creation results in a to and a fro trip
                            List<TripDto> trips = new ArrayList<>(2);
                            Trip toTrip = new Trip();
                            toTrip.setSourceStop(sourceStop);
                            toTrip.setDestStop(destinationStop);
                            toTrip.setAgency(agency);
                            toTrip.setBus(bus);
                            toTrip.setJourneyTime(tripDto.getJourneyTime());
                            toTrip.setFare(tripDto.getFare());
                            trips.add(TripMapper.toTripDto(tripRepository.save(toTrip)));

                            Trip froTrip = new Trip();
                            froTrip.setSourceStop(destinationStop);
                            froTrip.setDestStop(sourceStop);
                            froTrip.setAgency(agency);
                            froTrip.setBus(bus);
                            froTrip.setJourneyTime(tripDto.getJourneyTime());
                            froTrip.setFare(tripDto.getFare());
                            trips.add(TripMapper.toTripDto(tripRepository.save(froTrip)));
                            return trips;
                        }
                        throw exception(BUS, ENTITY_NOT_FOUND, tripDto.getBusCode());
                    }
                    throw exception(AGENCY, ENTITY_NOT_FOUND, tripDto.getAgencyCode());
                }
                throw exception(TRIP, ENTITY_EXCEPTION, "");
            }
            throw exception(STOP, ENTITY_NOT_FOUND, tripDto.getDestinationStopCode());
        }
        throw exception(STOP, ENTITY_NOT_FOUND, tripDto.getSourceStopCode());
    }


    public List<TripDto> getAgencyTrips(String agencyCode) {
        Agency agency = getAgency(agencyCode);
        if (agency != null) {
            List<Trip> agencyTrips = tripRepository.findByAgency(agency.getName());
            if (!agencyTrips.isEmpty()) {
                return agencyTrips
                        .stream()
                        .map(trip -> TripMapper.toTripDto(trip))
                        .collect(Collectors.toList());
            }
            return Collections.emptyList();
        }
        throw exception(AGENCY, ENTITY_NOT_FOUND, agencyCode);
    }


    public List<TripDto> getAvailableTripsBetweenStops(String sourceStopCode, String destinationStopCode) {
        List<Trip> availableTrips = findTripsBetweenStops(sourceStopCode, destinationStopCode);
        if (!availableTrips.isEmpty()) {
            return availableTrips
                    .stream()
                    .map(trip -> TripMapper.toTripDto(trip))
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public List<TripScheduleDto> getAvailableTripSchedules(String sourceStopCode, String destinationStopCode, String tripDate) {
        List<Trip> availableTrips = findTripsBetweenStops(sourceStopCode, destinationStopCode);
        if (!availableTrips.isEmpty()) {
            return availableTrips
                    .stream()
                    .map(trip -> getTripSchedule(TripMapper.toTripDto(trip), tripDate, true))
                    .filter(tripScheduleDto -> tripScheduleDto != null)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }


    public TripScheduleDto getTripSchedule(TripDto tripDto, String tripDate, boolean createSchedForTrip) {
        Optional<Trip> trip = tripRepository.findById(Long.valueOf(tripDto.getId()));
        if (trip.isPresent()) {
            Optional<TripSchedule> tripSchedule = Optional.ofNullable(tripScheduleRepository.findByTripDetailAndTripDate(trip.get(), tripDate));
            if (tripSchedule.isPresent()) {
                return TripScheduleMapper.toTripScheduleDto(tripSchedule.get());
            } else {
                if (createSchedForTrip) { //create the schedule
                    TripSchedule tripSchedule1 = new TripSchedule();
                    tripSchedule1.setTripDetail(trip.get());
                    tripSchedule1.setTripDate(tripDate);
                    tripSchedule1.setAvailableSeats(trip.get().getBus().getCapacity());
                    return TripScheduleMapper.toTripScheduleDto(tripScheduleRepository.save(tripSchedule1));
                } else {
                    throw exceptionWithId(TRIP, ENTITY_NOT_FOUND, "2", tripDto.getId(), tripDate);
                }
            }
        }
        throw exception(TRIP, ENTITY_NOT_FOUND, tripDto.getId());
    }


    @Transactional
    public TicketDto bookTicket(TripScheduleDto tripScheduleDto, UserDto userDto) {
        User user = getUser(userDto.getEmail());
        if (user != null) {
            Optional<TripSchedule> tripSchedule = tripScheduleRepository.findById(Long.valueOf(tripScheduleDto.getId()));
            if (tripSchedule.isPresent()) {
                Ticket ticket = new Ticket();
                ticket.setCancellable(false);
                ticket.setJourneyDate(tripSchedule.get().getTripDate());
                ticket.setPassenger(user);
                ticket.setTripSchedule(tripSchedule.get());
                ticket.setSeatNumber(tripSchedule.get().getTripDetail().getBus().getCapacity() - tripSchedule.get().getAvailableSeats());
                ticketRepository.save(ticket);
                tripSchedule.get().setAvailableSeats(tripSchedule.get().getAvailableSeats() - 1); //reduce availability by 1
                tripScheduleRepository.save(tripSchedule.get());//update schedule
                return TicketMapper.toTicketDto(ticket);
            }
            throw exceptionWithId(TRIP, ENTITY_NOT_FOUND, "2", tripScheduleDto.getTripId(), tripScheduleDto.getTripDate());
        }
        throw exception(USER, ENTITY_NOT_FOUND, userDto.getEmail());
    }


    private List<Trip> findTripsBetweenStops(String sourceStopCode, String destinationStopCode) {
        Optional<Stop> sourceStop = Optional
                .ofNullable(stopRepository.findByCode(sourceStopCode));
        if (sourceStop.isPresent()) {
            Optional<Stop> destStop = Optional
                    .ofNullable(stopRepository.findByCode(destinationStopCode));
            if (destStop.isPresent()) {
                List<Trip> availableTrips = tripRepository.findAllBySourceStopAndDestStop(sourceStop.get(), destStop.get());
                if (!availableTrips.isEmpty()) {
                    return availableTrips;
                }
                return Collections.emptyList();
            }
            throw exception(STOP, ENTITY_NOT_FOUND, destinationStopCode);
        }
        throw exception(STOP, ENTITY_NOT_FOUND, sourceStopCode);
    }


    private User getUser(String email) {
        return userRepository.findByEmail(email);
    }


    private Stop getStop(String stopCode) {
        return stopRepository.findByCode(stopCode);
    }


    private Bus getBus(String busCode) {
        return busRepository.findByCode(busCode);
    }


    private Agency getAgency(String agencyCode) {
        return agencyRepository.findByCode(agencyCode);
    }


    private RuntimeException exception(EntityType entityType, ExceptionType exceptionType, String... args) {
        return BRSException.throwException(entityType, exceptionType, args);
    }


    private RuntimeException exceptionWithId(EntityType entityType, ExceptionType exceptionType, String id, String... args) {
        return BRSException.throwExceptionWithId(entityType, exceptionType, id, args);
    }
}
