package com.qathrin.bus.controller;

import com.qathrin.bus.model.TripSchedule;
import com.qathrin.bus.service.TripScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/tripS")
public class TripScheduleController {
	@Autowired
	TripScheduleService tripScheduleService;

	@CrossOrigin
	@GetMapping("/")
	public List<TripSchedule> List() {
		return tripScheduleService.ListAllTripSchedule();
	}

	@CrossOrigin
	@GetMapping("/tripS/{id}")
	public ResponseEntity<TripSchedule> tripSchedule(@PathVariable Integer id) {
		try {
			TripSchedule tripSchedule = tripScheduleService.getTripSchedule(id);
			return new ResponseEntity<TripSchedule>(tripSchedule, HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<TripSchedule>(HttpStatus.NOT_FOUND);
		}
	}

	@CrossOrigin
	@PostMapping("/tripS")
	public ResponseEntity<?> tripSchedule(@RequestBody TripSchedule tripSchedule) {
		tripScheduleService.saveTripSchedule(tripSchedule);
		return new ResponseEntity<>("Success Adding Data", HttpStatus.OK);
	}

	@CrossOrigin
	@PutMapping("/tripS/{id}")
	public ResponseEntity<?> update(@RequestBody TripSchedule tripSchedule, @PathVariable Integer id) {
		try {
			TripSchedule existTripSchedule = tripScheduleService.getTripSchedule(id);
			if (!Objects.equals(existTripSchedule.getId(), id)) {
				return new ResponseEntity<>("ID Empty!", HttpStatus.OK);
			}
			tripSchedule.setId(Long.valueOf(id));
			tripScheduleService.saveTripSchedule(tripSchedule);
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
}
