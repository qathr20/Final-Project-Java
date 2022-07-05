package com.qathrin.bus.service;

import com.qathrin.bus.model.Agency;
import com.qathrin.bus.repository.AgencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class AgencyService {

    @Autowired
    AgencyRepository agencyRepository;

    public List<Agency> getAllAgency(){

        return agencyRepository.findAll();
    }
}
