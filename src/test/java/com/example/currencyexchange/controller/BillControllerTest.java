package com.example.currencyexchange.controller;


import com.example.currencyexchange.enums.Category;
import com.example.currencyexchange.enums.UserType;
import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.model.Item;
import com.example.currencyexchange.model.User;
import com.example.currencyexchange.service.BillService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        // Create sample Bill with items
        User user = new User(UserType.EMPLOYEE, 3);
        List<Item> items = List.of(
                new Item("Laptop",  Category.ELECTRONICS,1000.0, 2),
                new Item("Apple",  Category.GROCERY,5.0, 1)
        );
        testBill = new Bill(items, 2005.0, "USD", "INR", user);
    }

    @Test
    void testCalculateBill_Success() throws Exception {
        // Mock service returning calculated amount
        when(billService.calculateNetAmount(any(Bill.class))).thenReturn(108315.0);

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"items\": [\n" +
                                "        { \"name\": \"Laptop\", \"price\": 1000.0, \"category\": \"ELECTRONICS\", \"quantity\": 2 },\n" +
                                "        { \"name\": \"Apple\", \"price\": 5.0, \"category\": \"GROCERY\", \"quantity\": 1 }\n" +
                                "    ],\n" +
                                "    \"totalAmount\": 2005.0,\n" +
                                "    \"originalCurrency\": \"USD\",\n" +
                                "    \"targetCurrency\": \"INR\",\n" +
                                "    \"user\": { \"userType\": \"EMPLOYEE\", \"customerTenure\": 3 }\n" +
                                "}")
                )
                .andExpect(status().isOk())
                .andExpect(content().string("108315.0"));
    }

    @Test
    void testCalculateBill_CurrencyConversionException() throws Exception {
        // Mock service throwing CurrencyConversionException
        when(billService.calculateNetAmount(any(Bill.class)))
                .thenThrow(new CurrencyConversionException(HttpStatus.BAD_REQUEST, "Invalid currency conversion"));

        mockMvc.perform(post("/api/calculate")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "    \"items\": [\n" +
                                "        { \"name\": \"Laptop\", \"price\": 1000.0, \"category\": \"ELECTRONICS\", \"quantity\": 2 },\n" +
                                "        { \"name\": \"Apple\", \"price\": 5.0, \"category\": \"GROCERY\", \"quantity\": 1 }\n" +
                                "    ],\n" +
                                "    \"totalAmount\": 2005.0,\n" +
                                "    \"originalCurrency\": \"USD\",\n" +
                                "    \"targetCurrency\": \"INR\",\n" +
                                "    \"user\": { \"userType\": \"EMPLOYEE\", \"customerTenure\": 3 }\n" +
                                "}")
                )
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid currency conversion"));
    }
}
