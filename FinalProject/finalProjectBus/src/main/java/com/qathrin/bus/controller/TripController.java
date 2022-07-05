package com.qathrin.bus.controller;


import com.qathrin.bus.model.Trip;
import com.qathrin.bus.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/trip")
public class TripController {

    @Autowired
    TripService tripService;

    @CrossOrigin
    @GetMapping("/")
    public List<Trip> List(){
        return tripService.ListAllTrip();
    }
    @CrossOrigin
    @GetMapping("/trip/{id}")
    public ResponseEntity<Trip> trip (@PathVariable Integer id){
        try{
            Trip trip = tripService.getTrip(id);
            return new ResponseEntity<Trip>(trip, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<Trip>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @PostMapping("/trip")
    public ResponseEntity<?>trip (@RequestBody Trip trip){
        tripService.saveTrip(trip);
        return new ResponseEntity<>("Success Adding Data", HttpStatus.OK);
    }
    @CrossOrigin
    @PutMapping("/trip/{id}")
    public ResponseEntity<?>update(@RequestBody Trip trip, @PathVariable Integer id){
        try {
            Trip existTrip = tripService.getTrip(id);
            if (!Objects.equals(existTrip.getId().intValue(), id)){
                return new ResponseEntity<>("ID Empty!", HttpStatus.OK);
            }
            trip.setId(Long.valueOf(id));
            tripService.saveTrip(trip);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @DeleteMapping("trip/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        tripService.deleteTrip(id);
        return  new ResponseEntity<>("Success Deleting Data"+id, HttpStatus.OK);
    }

}
