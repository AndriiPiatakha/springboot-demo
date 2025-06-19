package com.itbulls.springdemo.loadbalancer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users-lb")
public class UserLbController {

    @Value("${server.port}")
    private String port;

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("Users from instance on port " + port);
    }
}
