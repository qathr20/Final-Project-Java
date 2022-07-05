package com.qathrin.bus.repository;

import com.qathrin.bus.model.Agency;
import com.qathrin.bus.model.Bus;
import com.qathrin.bus.model.Stop;
import com.qathrin.bus.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

	List<Trip> findByFareBetween(Integer minFare, Integer maxFare);

	List<Trip> findByJourneyTimeBetween(Integer minJourneyTime, Integer maxJourneyTime);

	List<Trip> findByBus(Bus bus);

	@Query(value = "SELECT * FROM tb_trip t INNER JOIN tb_stop s on t.dest_stop_id = s.id WHERE LOWER(s.name) LIKE %:destStop%", nativeQuery = true)
	List<Trip> findByDestStop(String destStop);

	@Query(value = "SELECT * FROM tb_trip t INNER JOIN tb_stop s on t.source_stop_id = s.id WHERE LOWER(s.name) LIKE %:sourceStop%", nativeQuery = true)
	List<Trip> findBySourceStop(String sourceStop);

	@Query(value = "SELECT * FROM tb_trip t INNER JOIN tb_agency s on t.agency_id = s.id WHERE LOWER(s.name) LIKE %:agency%", nativeQuery = true)
	List<Trip> findByAgency(String agency);

	@Query(value = "SELECT DISTINCT * FROM tb_trip WHERE source_stop_id = :sourceStopId AND dest_stop_id = :destStopId", nativeQuery = true)
	List<Trip> findTripsByStops(Integer sourceStopId, Integer destStopId);

	Trip findBySourceStopAndDestStopAndBus(Stop source, Stop destination, Bus bus);

	List<Trip> findAllBySourceStopAndDestStop(Stop source, Stop destination);

	List<Trip> findByAgency(Agency agency);
}