package com.itbulls.springdemo.resilience4j.bulkhead;

import java.util.concurrent.CompletableFuture;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BulkheadController {

    private final BulkheadService syncService;
    private final AsyncBulkheadService asyncService;

    public BulkheadController(BulkheadService syncService, AsyncBulkheadService asyncService) {
        this.syncService = syncService;
        this.asyncService = asyncService;
    }

    @GetMapping("/demo-bulkhead-sync")
    public String callSync() {
        return syncService.processSyncRequest();
    }

    @GetMapping("/demo-bulkhead-async")
    public CompletableFuture<String> callAsync() {
        return asyncService.processAsyncRequest();
    }
}
