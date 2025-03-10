package com.example.currencyexchange.config;

import com.example.currencyexchange.service.BillService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BillService billService; // Mocking BillService to prevent NullPointerException

    @Test
    void calculateBillShouldRequireAuthentication() throws Exception {
        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0, \"currency\": \"USD\"}")) // Sample valid payload
                .andExpect(status().isUnauthorized()); // Expect 401 Unauthorized
    }

    @Test
    @WithMockUser(username = "user", roles = "USER") // Simulate authenticated user
    void calculateBillShouldBeAccessibleWithAuthentication() throws Exception {
        // Mock service behavior to return a fixed value
        when(billService.calculateNetAmount(org.mockito.ArgumentMatchers.any())).thenReturn(120.0);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"amount\": 100.0, \"currency\": \"USD\"}")) // Ensure valid payload
                .andExpect(status().isOk()); // Expect 200 OK
    }
}
