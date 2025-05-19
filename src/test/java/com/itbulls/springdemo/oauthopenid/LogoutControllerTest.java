package com.itbulls.springdemo.oauthopenid;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.util.UriUtils;

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration(initializers = LogoutControllerTest.PropertyOverrideInitializer.class)
class LogoutControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void logout_redirectsToAuth0LogoutAndClearsContext() throws Exception {
        // Add logged in user
        Authentication auth = new TestingAuthenticationToken("user", "password");
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        String returnTo = UriUtils.encode("http://localhost:8080/", StandardCharsets.UTF_8);
        String expectedRedirect = "https://example.auth0.com/v2/logout?client_id=test-client-id&returnTo=" + returnTo;

        mockMvc.perform(get("/custom-logout"))
               .andExpect(status().is3xxRedirection())
               .andExpect(redirectedUrl(expectedRedirect));

        // Check that the context is cleared
        assert SecurityContextHolder.getContext().getAuthentication() == null;
    }

    // Override properties that are used in the controller
    static class PropertyOverrideInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext context) {
            TestPropertyValues.of(
                "spring.security.oauth2.client.registration.auth0.client-id=test-client-id",
                "spring.security.oauth2.client.provider.auth0.issuer-uri=https://example.auth0.com/"
            ).applyTo(context.getEnvironment());
        }
    }
}
