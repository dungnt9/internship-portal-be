package com.dungnguyen.evaluation_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluation")
public class EvaluationController {
    @GetMapping("/hello")
    public ResponseEntity<String> getAnonymous() {
        System.out.println("Hello evaluation service");
        return ResponseEntity.ok("Welcome");
    }
}
