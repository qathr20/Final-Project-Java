package com.qathrin.bus.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qathrin.bus.model.Stop;

public interface StopRepository extends JpaRepository<Stop, Long> {
	List<Stop> findByName(String name);

	Stop findByCode(String code);
}
