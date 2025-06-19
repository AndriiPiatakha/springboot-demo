package com.itbulls.springdemo.gateway;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserRestController {

    @GetMapping("/{id}")
    public ResponseEntity<String> getUserById(@PathVariable String id) {
        return ResponseEntity.ok("User with ID: " + id);
    }

    @GetMapping
    public ResponseEntity<String> getAllUsers() {
        return ResponseEntity.ok("List of all users");
    }
}