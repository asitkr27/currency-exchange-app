package com.example.currencyexchange.controller;

import com.example.currencyexchange.enums.Category;
import com.example.currencyexchange.enums.UserType;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.model.Item;
import com.example.currencyexchange.model.User;
import com.example.currencyexchange.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class BillControllerTest {

    private MockMvc mockMvc;

    @Mock
    private BillService billService;

    @InjectMocks
    private BillController billController;

    private Bill testBill;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(billController).build();

        // Create sample Bill data
        User user = new User(UserType.EMPLOYEE, 3);
        List<Item> items = List.of(
                new Item("Laptop", Category.ELECTRONICS, 1000.0, 2),
                new Item("Apple", Category.GROCERY, 5.0, 1)
        );
        testBill = new Bill(items, 2000, "USD", "INR", user);
    }

    @Test
    void testCalculateBill_Success() throws Exception {
        // Mock service response for any Bill instance
        when(billService.calculateNetAmount(any(Bill.class))).thenReturn(143000.75);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                    "items": [
                        { "name": "Laptop", "price": 1000.0, "category": "ELECTRONICS", "quantity": 2 },
                        { "name": "Apple", "price": 5.0, "category": "GROCERY", "quantity": 1 }
                    ],
                    "originalCurrency": "USD",
                    "targetCurrency": "INR",
                    "user": { "userType": "EMPLOYEE", "customerTenure": 3 }
                }
            """))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(143000.75));
    }
}
