package com.example.demo.controller;

import com.example.demo.service.DoNotDisturbService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DoNotDisturbController {

    @Autowired
    private DoNotDisturbService doNotDisturbService;

    @GetMapping("/checkDndStatus")
    public int checkStatus(@RequestParam String customerNo) {
        return doNotDisturbService.checkStatus(customerNo);
    }
}
