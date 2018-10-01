package com.example.demo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/web")
public class WebController {
    @GetMapping("")
    public String index(ModelMap map) {
        map.put("title", "guoqin kuail");
        return "index";
    }
@GetMapping("/upload")
    public String upload(ModelMap map) {
        return "upload";
    }

    @RequestMapping("/error")
    public String error( ModelMap map) {
            throw new RuntimeException("test exception");
        }
    }

