package com.itbulls.springdemo.structuredlogging;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class PaymentsLoggingController {

	private static final Logger log = LoggerFactory.getLogger(PaymentsLoggingController.class);
    private final RestTemplate restTemplate;
    
    public PaymentsLoggingController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/payments-logging")
    public ResponseEntity<String> getPayments() {
        log.info("Getting payments");
        String fraudResult = restTemplate.getForObject("http://localhost:8081/fraud-check", String.class);
        String response = "Payment OK → " + fraudResult;

        return ResponseEntity.ok()
                .contentType(MediaType.TEXT_PLAIN)
                .body(response);
    }
}
