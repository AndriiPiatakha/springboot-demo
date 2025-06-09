package com.itbulls.springdemo.resilience4j.circuitbreaker;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoCbController {

    private final DemoService demoService;

    public DemoCbController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/demo-cb")
    public String testCircuitBreaker() {
        return demoService.callServiceWithCircuitBreaker();
    }
}