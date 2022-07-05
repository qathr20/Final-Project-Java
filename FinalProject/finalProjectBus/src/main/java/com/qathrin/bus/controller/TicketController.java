package com.qathrin.bus.controller;

import com.qathrin.bus.model.Ticket;
import com.qathrin.bus.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/ticket")
public class TicketController {
    @Autowired
    TicketService ticketService;

    @CrossOrigin
    @GetMapping("/")
    public List<Ticket> List(){
        return ticketService.ListAllTicket();
    }
    @CrossOrigin
    @GetMapping("/ticket/{id}")
    public ResponseEntity<Ticket> ticket (@PathVariable Integer id){
        try{
            Ticket ticket = ticketService.getTicket(id);
            return new ResponseEntity<Ticket>(ticket, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<Ticket>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @PostMapping("/ticket")
    public ResponseEntity<?>ticket (@RequestBody Ticket ticket){
        ticketService.saveTicket(ticket);
        return new ResponseEntity<>("Success Adding Data", HttpStatus.OK);
    }
    @CrossOrigin
    @PutMapping("/ticket/{id}")
    public ResponseEntity<?>update(@RequestBody Ticket ticket, @PathVariable Integer id){
        try {
            Ticket existTicket = ticketService.getTicket(id);
            if (!Objects.equals(existTicket.getId(), id)){
                return new ResponseEntity<>("ID Empty!", HttpStatus.OK);
            }
            ticket.setId(Long.valueOf(id));
            ticketService.saveTicket(ticket);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @DeleteMapping("ticket/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        ticketService.deletTicket(id);
        return  new ResponseEntity<>("Success Deleting Data"+id, HttpStatus.OK);
    }
}
