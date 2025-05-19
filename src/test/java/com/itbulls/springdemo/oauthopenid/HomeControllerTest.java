package com.itbulls.springdemo.oauthopenid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void homeRedirectsToLogin_whenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrlPattern("**/oauth2/authorization/**")); // path to login for OIDC
    }
    
    @Test
    void home_showsUserInfo_whenAuthenticated() throws Exception {
        var user = Mockito.mock(OidcUser.class);
        String testUserEmail = "user@example.com";
		Mockito.when(user.getEmail()).thenReturn(testUserEmail);
        Mockito.when(user.getClaims()).thenReturn(Map.of("email", testUserEmail));

        var auth = new TestingAuthenticationToken(user, null);
        auth.setAuthenticated(true);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(auth);
        SecurityContextHolder.setContext(context);

        mockMvc.perform(get("/"))
               .andExpect(status().isOk())
               .andExpect(model().attributeExists("user"))
               .andExpect(model().attribute("user", user));
    }
}