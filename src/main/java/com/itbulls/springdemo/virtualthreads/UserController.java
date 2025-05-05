package com.itbulls.springdemo.virtualthreads;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> fetchData() {
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(userService.fetchAllData());
    }
    
    
    @GetMapping("/printThreadName")
    public ResponseEntity<String> getThreadName() {
        String threadInfo = Thread.currentThread().toString();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(threadInfo);
    }
}
