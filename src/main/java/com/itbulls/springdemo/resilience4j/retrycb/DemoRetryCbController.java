package com.itbulls.springdemo.resilience4j.retrycb;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoRetryCbController {

    private final ReliableService reliableService;

    public DemoRetryCbController(ReliableService reliableService) {
        this.reliableService = reliableService;
    }

    @GetMapping("/demo-retrycb")
    public String demo() {
        return reliableService.callExternal();
    }
}