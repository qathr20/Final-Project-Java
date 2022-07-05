package com.qathrin.bus.service;

import com.qathrin.bus.model.Trip;
import com.qathrin.bus.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class TripService {
    @Autowired
    private TripRepository tripRepository;

    public List<Trip> ListAllTrip(){

        return  tripRepository.findAll();
    }
    public void saveTrip(Trip trip){

        tripRepository.save(trip);
    }
    public Trip getTrip(Integer id){

        return tripRepository.findById(Long.valueOf(id)).get();
    }
    public void deleteTrip(Integer id){

        tripRepository.deleteById(Long.valueOf(id));
    }
}
