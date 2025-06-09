package com.itbulls.springdemo.resilience4j.retry;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RetryController {

    private final RetryDemoService retryDemoService;

    public RetryController(RetryDemoService retryDemoService) {
        this.retryDemoService = retryDemoService;
    }

    @GetMapping("/demo-retry")
    public String retryEndpoint() {
        return retryDemoService.callWithRetry();
    }
}
