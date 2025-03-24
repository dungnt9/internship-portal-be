package com.nhp.demo1.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo1")
public class Demo1Controller {
    @GetMapping("/nhp")
    public ResponseEntity<String> getAnonymous() {
        System.out.println("Hello demo1");

        return ResponseEntity.ok("Welcome");
    }
}
