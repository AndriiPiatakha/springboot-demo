package com.itbulls.springdemo.ratelimiting;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrintThreadNameController {
    
    @GetMapping("/printThreadName-rate-limit")
    public ResponseEntity<String> getThreadName() {
        String threadInfo = Thread.currentThread().toString();
        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(threadInfo);
    }
}
