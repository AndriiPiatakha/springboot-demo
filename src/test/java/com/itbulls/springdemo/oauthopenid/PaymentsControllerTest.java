package com.itbulls.springdemo.oauthopenid;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.Matchers.containsInAnyOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PaymentsController.class)
class PaymentsControllerTest {

	@MockitoBean
    RestTemplateBuilder restTemplateBuilder;
	
    @Autowired
    MockMvc mockMvc;

    @Test
    @WithMockUser(authorities = "read:payments")
    void getPayments_withAuthority_returnsPayments() throws Exception {
        mockMvc.perform(get("/payments"))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$[0]").value("txn-001"));
    }

    @Test
    @WithMockUser(authorities = "some:other")
    void getPayments_withoutAuthority_forbidden() throws Exception {
        mockMvc.perform(get("/payments"))
               .andExpect(status().isForbidden());
    }
    
    @Test
    void payments_unauthenticated_returns401() throws Exception {
        mockMvc.perform(get("/payments"))
               .andExpect(status().isUnauthorized());
    }

	@Test
	@WithMockUser(authorities = {"read:payments", "admin"})
	void debug_returnsAuthorities() throws Exception {
	    mockMvc.perform(get("/debug"))
	           .andExpect(status().isOk())
	           .andExpect(jsonPath("$", containsInAnyOrder("read:payments", "admin")));
	}
	
	@Test
	@WithMockUser(roles = "ADMIN")
	void admin_accessGranted() throws Exception {
	    mockMvc.perform(get("/admin")).andExpect(status().isOk());
	}
}
