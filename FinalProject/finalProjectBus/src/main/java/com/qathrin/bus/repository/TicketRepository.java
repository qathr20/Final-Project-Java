package com.qathrin.bus.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.qathrin.bus.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

}
