package com.itbulls.springdemo.resilience4j.ratelimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RateLimiterController {

    private final RateLimitedService service;

    public RateLimiterController(RateLimitedService service) {
        this.service = service;
    }

    @GetMapping("/demo-ratelimited")
    public String get() {
        return service.getData();
    }
}