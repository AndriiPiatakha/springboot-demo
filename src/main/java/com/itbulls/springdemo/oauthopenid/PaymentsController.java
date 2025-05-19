package com.itbulls.springdemo.oauthopenid;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PaymentsController {

    @GetMapping("/payments")
    @PreAuthorize("hasAuthority('read:payments')")
    public List<String> getPayments() {
        return List.of("txn-001", "txn-002", "txn-003");
    }
    
    @GetMapping("/debug")
    public List<String> debug(Authentication auth) {
        return auth.getAuthorities()
                   .stream()
                   .map(GrantedAuthority::getAuthority)
                   .toList();
    }
    
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String admin() {
        return "admin-zone";
    }
}