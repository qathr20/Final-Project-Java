package com.qathrin.bus.controller;

import com.qathrin.bus.model.Bus;
import com.qathrin.bus.service.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/bus")
public class BusController {

    @Autowired
    BusService busService;

    @CrossOrigin
    @GetMapping("/")
    public List<Bus> List(){
        return busService.ListAllBus();
    }
    @CrossOrigin
    @GetMapping("/bus/{id}")
    public ResponseEntity<Bus> bus (@PathVariable Integer id){
        try{
            Bus bus = busService.getBus(id);
            return new ResponseEntity<Bus>(bus, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<Bus>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @PostMapping("/bus")
    public ResponseEntity<?>bus (@RequestBody Bus bus){
        busService.saveBus(bus);
        return new ResponseEntity<>("Success Adding Data", HttpStatus.OK);
    }
    @CrossOrigin
    @PutMapping("/bus/{id}")
    public ResponseEntity<?>update(@RequestBody Bus bus, @PathVariable Integer id){
        try {
            Bus existBus = busService.getBus(id);
            if (!Objects.equals(existBus.getId().intValue(), id)){
                return new ResponseEntity<>("ID Empty!", HttpStatus.OK);
            }
            bus.setId(Long.valueOf(id));
            busService.saveBus(bus);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    @CrossOrigin
    @DeleteMapping("bus/{id}")
    public ResponseEntity<?>delete(@PathVariable Integer id){
        busService.deleteBus(id);
        return  new ResponseEntity<>("Success Deleting Data"+id, HttpStatus.OK);
    }
}
