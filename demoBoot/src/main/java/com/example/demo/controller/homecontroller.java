package com.example.demo.controller;

import com.example.demo.bean.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/home")
public class homecontroller {
    private static final Logger logger = LoggerFactory.getLogger(homecontroller.class);

    @Value(value = "${dharma.title}")
    private String title;

    @Value(value = "${dharma.teacher}")
    private String teacher;

    @Value(value = "${dharma.project}")
    private String project;

    @Value(value = "${dharma.number}")
    private Integer number;

    @Value(value = "#{product}")
    private Product product;

    @Bean
    public Product product() {
        return new Product(666L, "Thanks god you are coming", 9999999.0);
    }

    @RequestMapping
    public String home() {
        logger.error("this is a log error test");
        logger.info("this is a log info test");
        return "How are you!";
    }

    @GetMapping("/get")
    public HashMap<String, Object> getProducts(@RequestParam String name) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("name", name);
        map.put("teacher", teacher);
        map.put("project", project);
        map.put("number", number);
        map.put("product", product);

        return map;
    }

    @GetMapping("/product/{name}/{price}")
    public Product getProduct(@PathVariable String name, @PathVariable Double price) {
        return new Product(999L, name, price);
    }
}