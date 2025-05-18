package com.itbulls.springdemo.oauthopenid;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.util.UriUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LogoutController {

    @Value("${spring.security.oauth2.client.registration.auth0.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.provider.auth0.issuer-uri}")
    private String issuer;

    @GetMapping("/custom-logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Authentication auth) {
        new SecurityContextLogoutHandler().logout(request, response, auth);
        String returnTo = UriUtils.encode("http://localhost:8080/", StandardCharsets.UTF_8);
        String logoutUrl = issuer + "v2/logout?client_id=" + clientId + "&returnTo=" + returnTo;

        return "redirect:" + logoutUrl;
    }

}
