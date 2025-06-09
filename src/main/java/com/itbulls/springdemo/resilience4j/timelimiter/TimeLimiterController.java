package com.itbulls.springdemo.resilience4j.timelimiter;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;

@RestController
public class TimeLimiterController {

    private final TimeLimiterDemoService demoService;

    public TimeLimiterController(TimeLimiterDemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping("/demo-timelimiter")
    public CompletableFuture<String> handle() {
        return demoService.callWithTimeout();
    }
}
