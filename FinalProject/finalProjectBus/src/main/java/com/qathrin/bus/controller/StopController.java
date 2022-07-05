package com.qathrin.bus.controller;

import com.qathrin.bus.model.Stop;
import com.qathrin.bus.service.StopService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/stop")
public class StopController {

    @Autowired
    StopService stopService;

    @CrossOrigin
    @GetMapping("/")
    public List<Stop> getAllStop(){
    return stopService.getAllStop();
    }

    @CrossOrigin
    @GetMapping("/stop/{id}")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<Stop>stop(@PathVariable Integer id){
        try{
            Stop stop = stopService.getStop(id);
            return new ResponseEntity<Stop>(stop, HttpStatus.OK);
        }catch (NoSuchElementException e){
            return new ResponseEntity<Stop>(HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin
    @PostMapping("/")
    @ApiOperation(value = "", authorizations = {@Authorization(value = "apiKey")})
    public ResponseEntity<?> stop (@RequestBody Stop stop){
        stopService.saveStop(stop);
        return new ResponseEntity<>("Success Adding Data", HttpStatus.OK);
    }

    @CrossOrigin
    @DeleteMapping("/stop/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        stopService.deleteStop(id);
        return  new ResponseEntity<>("Success Deleting Data with ID"+id, HttpStatus.OK);
    }





}
