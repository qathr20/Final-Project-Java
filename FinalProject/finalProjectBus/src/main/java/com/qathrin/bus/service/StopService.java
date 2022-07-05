package com.qathrin.bus.service;

import com.qathrin.bus.model.Stop;
import com.qathrin.bus.model.User;
import com.qathrin.bus.repository.StopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class StopService {

    @Autowired
    StopRepository stopRepository;

    public List<Stop> getAllStop(){
        return stopRepository.findAll();
    }
    public Stop getStop(Integer id){
        return stopRepository.findById(Long.valueOf(id)).get();
    }
    public void saveStop(Stop stop) {
        stopRepository.save(stop);
    }
    public void deleteStop(Integer id) {
        stopRepository.deleteById(Long.valueOf(id));
    }
}
