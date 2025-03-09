package com.example.currencyexchange.service;

import com.example.currencyexchange.dto.ExchangeRateResponse;
import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.serviceimpl.CurrencyExchangeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyExchangeServiceImplTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CurrencyExchangeServiceImpl currencyExchangeService;

    private ExchangeRateResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = new ExchangeRateResponse();
        mockResponse.setBase_code("USD");
        Map<String, Double> conversionRates = new HashMap<>();
        conversionRates.put("EUR", 0.85);
        conversionRates.put("GBP", 0.75);
        mockResponse.setConversion_rates(conversionRates);
    }


    @Test
    void getExchangeRate_success() {
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        Double exchangeRate = currencyExchangeService.getExchangeRate("USD", "EUR");

        assertEquals(0.85, exchangeRate);
        verify(restTemplate).getForObject("https://v6.exchangerate-api.com/v6/dce31f10462e2ee38f0b1ba5/latest/USD", ExchangeRateResponse.class);
    }

    @Test
    void getExchangeRate_httpServerError() {
        HttpServerErrorException exception = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "Server Error");
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenThrow(exception);

        assertThrows(CurrencyConversionException.class, () -> currencyExchangeService.getExchangeRate("USD", "EUR"));
    }

    @Test
    void getExchangeRate_invalidResponse() {
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(null);

        assertThrows(CurrencyConversionException.class, () -> currencyExchangeService.getExchangeRate("USD", "EUR"));

        ExchangeRateResponse responseWithoutBaseCode = new ExchangeRateResponse();
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(responseWithoutBaseCode);
        assertThrows(CurrencyConversionException.class, () -> currencyExchangeService.getExchangeRate("USD", "EUR"));

        ExchangeRateResponse responseWithoutConversionRates = new ExchangeRateResponse();
        responseWithoutConversionRates.setBase_code("USD");
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(responseWithoutConversionRates);
        assertThrows(CurrencyConversionException.class, () -> currencyExchangeService.getExchangeRate("USD", "EUR"));

    }

    @Test
    void getExchangeRate_invalidCurrency() {
        when(restTemplate.getForObject(anyString(), eq(ExchangeRateResponse.class))).thenReturn(mockResponse);

        assertThrows(CurrencyConversionException.class, () -> currencyExchangeService.getExchangeRate("USD", "JPY"));
    }
}