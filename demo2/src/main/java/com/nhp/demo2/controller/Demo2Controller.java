package com.nhp.demo2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo2")
public class Demo2Controller {
    @GetMapping("/nhp")
    public ResponseEntity<String> getAnonymous() {
        System.out.println("Hello demo2");
        return ResponseEntity.ok("Welcome");
    }
}
