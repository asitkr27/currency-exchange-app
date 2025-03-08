package com.example.currencyexchange.service;

import com.example.currencyexchange.dto.ExchangeRateResponse;
import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.serviceimpl.CurrencyExchangeServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/dce31f10462e2ee38f0b1ba5/latest/";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetExchangeRate_Success() {
        // Mock API response
        ExchangeRateResponse mockResponse = new ExchangeRateResponse();
        mockResponse.setBase_code("USD");

        Map<String, Double> conversionRates = new HashMap<>();
        conversionRates.put("INR", 87.1291);
        mockResponse.setConversion_rates(conversionRates);

        String url = API_URL + "USD";
        when(restTemplate.getForObject(url, ExchangeRateResponse.class)).thenReturn(mockResponse);

        // Call method
        Double exchangeRate = currencyExchangeService.getExchangeRate("USD", "INR");

        // Assertions
        Assertions.assertNotNull(exchangeRate);
        Assertions.assertEquals(87.1291, exchangeRate);
    }

    @Test
    void testGetExchangeRate_InvalidCurrencyCode() {
        // Simulate a 400 Bad Request error
        String url = API_URL + "XYZ";
        when(restTemplate.getForObject(url, ExchangeRateResponse.class))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Invalid currency code"));

        // Assert exception
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class,
                () -> currencyExchangeService.getExchangeRate("XYZ", "INR"));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertTrue(exception.getMessage().contains("Invalid currency code"));
    }

    @Test
    void testGetExchangeRate_ServerError() {
        // Simulate a 500 Server Error
        String url = API_URL + "USD";
        when(restTemplate.getForObject(url, ExchangeRateResponse.class))
                .thenThrow(new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"));

        // Assert exception
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class,
                () -> currencyExchangeService.getExchangeRate("USD", "INR"));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        Assertions.assertTrue(exception.getMessage().contains("Server error while fetching exchange rate"));
    }

    @Test
    void testGetExchangeRate_InvalidApiResponse() {
        // Mock an empty response
        String url = API_URL + "USD";
        when(restTemplate.getForObject(url, ExchangeRateResponse.class)).thenReturn(null);

        // Assert exception
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class,
                () -> currencyExchangeService.getExchangeRate("USD", "INR"));

        Assertions.assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        Assertions.assertTrue(exception.getMessage().contains("Invalid response from exchange rate API"));
    }

    @Test
    void testGetExchangeRate_UnexpectedError() {
        // Simulate a generic runtime exception
        String url = API_URL + "USD";
        when(restTemplate.getForObject(url, ExchangeRateResponse.class)).thenThrow(new RuntimeException("Unexpected error"));

        // Assert exception
        CurrencyConversionException exception = assertThrows(CurrencyConversionException.class,
                () -> currencyExchangeService.getExchangeRate("USD", "INR"));

        Assertions.assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatus());
        Assertions.assertTrue(exception.getMessage().contains("Unexpected error occurred while fetching exchange rate"));
    }
}
