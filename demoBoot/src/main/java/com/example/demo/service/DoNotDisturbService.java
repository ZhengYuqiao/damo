package com.example.demo.service;

import com.example.demo.entity.DoNotDisturb;
import com.example.demo.repository.DoNotDisturbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoNotDisturbService {

    @Autowired
    private DoNotDisturbRepository doNotDisturbRepository;

    public int checkStatus(String customerNo) {
        DoNotDisturb doNotDisturb = doNotDisturbRepository.findByCustomerNo(customerNo);
        if (doNotDisturb != null) {
            return 1;
        } else {
            return 0;
        }
    }
}
