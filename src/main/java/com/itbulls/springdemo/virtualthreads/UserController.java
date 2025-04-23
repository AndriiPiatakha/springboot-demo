package com.itbulls.springdemo.virtualthreads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Endpoint to fetch data
    @GetMapping("/fetchData")
    public String fetchData() {
        return userService.fetchAllData();
    }
    
    
    @GetMapping("/printThreadName")
    public String getThreadName() {
        return Thread.currentThread().toString();
    }
}
