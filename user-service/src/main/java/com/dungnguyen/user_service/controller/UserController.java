package com.dungnguyen.user_service.controller;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    @GetMapping("/hello")
    public ResponseEntity<String> getAnonymous() {
        System.out.println("Hello user service");

        return ResponseEntity.ok("Welcome");
    }
}
