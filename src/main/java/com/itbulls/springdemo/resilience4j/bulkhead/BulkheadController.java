package com.itbulls.springdemo.resilience4j.bulkhead;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BulkheadController {

    private final BulkheadService service;

    public BulkheadController(BulkheadService service) {
        this.service = service;
    }

    @GetMapping("/demo-bulkhead")
    public String call() {
        return service.processRequest();
    }
}
