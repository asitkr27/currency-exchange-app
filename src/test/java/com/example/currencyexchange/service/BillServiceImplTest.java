package com.example.currencyexchange.service;

import com.example.currencyexchange.enums.Category;
import com.example.currencyexchange.enums.UserType;
import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.model.Item;
import com.example.currencyexchange.model.User;
import com.example.currencyexchange.serviceimpl.BillServiceImpl;
import com.example.currencyexchange.serviceimpl.CurrencyExchangeServiceImpl;
import com.example.currencyexchange.util.DiscountCalculationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.MockedStatic;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BillServiceImplTest {

    @Mock
    private CurrencyExchangeServiceImpl currencyExchangeService; // Mock exchange service

    @InjectMocks
    private BillServiceImpl billService; // Inject mocks into BillService

    private Bill testBill;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Create sample Bill with items
        User user = new User(UserType.EMPLOYEE, 3);
        List<Item> items = List.of(
                new Item("Laptop", Category.ELECTRONICS, 1000.0, 2),
                new Item("Apple", Category.GROCERY, 5.0, 1)
        );
        testBill = new Bill(items, 2005.0, "USD", "INR", user);
    }

    @Test
    void testCalculateNetAmount_Success() {
        try (MockedStatic<DiscountCalculationUtil> mockedStatic = mockStatic(DiscountCalculationUtil.class)) {
            // Mock discount calculation
            mockedStatic.when(() -> DiscountCalculationUtil.applyDiscounts(any(Bill.class))).thenReturn(700.0);

            // Mock exchange rate API
            when(currencyExchangeService.getExchangeRate("USD", "INR")).thenReturn(83.0);

            // Calculate final amount
            double netAmount = billService.calculateNetAmount(testBill);
            assertEquals(108315.0, netAmount, "Final amount should be ₹108,315");
        }
    }

    @Test
    void testCalculateNetAmount_ZeroExchangeRate() {
        try (MockedStatic<DiscountCalculationUtil> mockedStatic = mockStatic(DiscountCalculationUtil.class)) {
            // Mock discount calculation
            mockedStatic.when(() -> DiscountCalculationUtil.applyDiscounts(any(Bill.class))).thenReturn(700.0);

            // Mock API returning invalid exchange rate
            when(currencyExchangeService.getExchangeRate("USD", "INR")).thenReturn(0.0);

            // Expect exception
            CurrencyConversionException exception = assertThrows(CurrencyConversionException.class, () ->
                    billService.calculateNetAmount(testBill));

            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
            assertTrue(exception.getMessage().contains("Invalid exchange rate"));
        }
    }

    @Test
    void testCalculateNetAmount_NegativeDiscount() {
        try (MockedStatic<DiscountCalculationUtil> mockedStatic = mockStatic(DiscountCalculationUtil.class)) {
            // Mock discount calculation returning higher than total amount
            mockedStatic.when(() -> DiscountCalculationUtil.applyDiscounts(any(Bill.class))).thenReturn(3000.0); // More than bill total

            // Mock exchange rate API
            when(currencyExchangeService.getExchangeRate("USD", "INR")).thenReturn(83.0);

            // Calculate final amount (should be ₹0 to prevent negatives)
            double netAmount = billService.calculateNetAmount(testBill);
            assertEquals(0, netAmount, "Net amount should not be negative");
        }
    }

    @Test
    void testCalculateNetAmount_NullExchangeRate() {
        try (MockedStatic<DiscountCalculationUtil> mockedStatic = mockStatic(DiscountCalculationUtil.class)) {
            // Mock discount calculation
            mockedStatic.when(() -> DiscountCalculationUtil.applyDiscounts(any(Bill.class))).thenReturn(700.0);

            // Mock API returning null exchange rate
            when(currencyExchangeService.getExchangeRate("USD", "INR")).thenReturn(null);

            // Expect exception
            CurrencyConversionException exception = assertThrows(CurrencyConversionException.class, () ->
                    billService.calculateNetAmount(testBill));

            assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        }
    }
}
