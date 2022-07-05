package com.qathrin.bus.repository;

import java.util.List;

import com.qathrin.bus.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qathrin.bus.model.TripSchedule;

@Repository
public interface TripScheduleRepository extends JpaRepository<TripSchedule, Long> {
	List<TripSchedule> findAllByTripDate(String tripDate);

	List<TripSchedule> findByTripDate(String tripDate);

	@Query(value = "SELECT DISTINCT * FROM trip_schedule WHERE trip_date = :tripDate", nativeQuery = true)
	List<TripSchedule> findTripScheduleByDate(String tripDate);

	TripSchedule findByTripDetailAndTripDate(Trip tripDetail, String tripDate);

}