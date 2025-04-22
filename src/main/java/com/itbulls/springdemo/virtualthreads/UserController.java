package com.itbulls.springdemo.virtualthreads;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Эндпоинт для фетчинга данных
    @GetMapping("/fetchData")
    public String fetchData() {
        return userService.fetchAllData();
    }
    
    // Эндпоинт для фетчинга данных
    @GetMapping("/fetchData2")
    public String fetchData2() {
        return userService.fetchDataForMultipleUsers(3);
    }
    
    @GetMapping("/name")
    public String getThreadName() {
        return Thread.currentThread().toString();
    }
    
    
    
    
    // ===================================
    
    @GetMapping("/virtual")
    public String runWithVirtualThreads() {
        return userService.fetchMassiveDataWithVirtualThreads();
    }
    
    @GetMapping("/regular")
    public String runWithRegularThreads() {
        return userService.fetchMassiveDataWithRegularThreads();
    }
}
