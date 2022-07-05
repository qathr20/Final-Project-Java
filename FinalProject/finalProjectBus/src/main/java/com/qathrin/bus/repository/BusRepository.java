package com.qathrin.bus.repository;

import java.util.List;

import com.qathrin.bus.model.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.qathrin.bus.model.Bus;

public interface BusRepository extends JpaRepository<Bus, Long> {
    @Query(value = "SELECT * FROM bus WHERE agency_id = :id", nativeQuery = true)
    List<Bus> findByAgencyId(Long id);

    Bus findByCode(String busCode);
    Bus findByCodeAndAgency(String busCode, Agency agency);
}
