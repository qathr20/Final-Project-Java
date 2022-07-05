package com.qathrin.bus.service;

import com.qathrin.bus.model.Bus;
import com.qathrin.bus.repository.BusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class BusService {

    @Autowired
    private BusRepository busRepository;

    public List<Bus> ListAllBus(){
        return  busRepository.findAll();
    }
    public void saveBus(Bus bus){
        busRepository.save(bus);
    }
    public Bus getBus(Integer id){
        return busRepository.findById(Long.valueOf(id)).get();
    }
    public void deleteBus(Integer id){
        busRepository.deleteById(Long.valueOf(id));
    }
}
