package com.example.currencyexchange.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();

    @Test
    void handleCurrencyConversionException_shouldReturnCorrectResponseEntity() {
        CurrencyConversionException exception = new CurrencyConversionException(HttpStatus.BAD_REQUEST, "Invalid currency");

        ResponseEntity<String> response = exceptionHandler.handleCurrencyConversionException(exception);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid currency", response.getBody());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerErrorResponseEntity() {
        Exception exception = new RuntimeException("Test exception");

        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: Test exception", response.getBody());
    }

    @Test
    void handleGenericException_withNullMessage_shouldReturnInternalServerErrorResponseEntity() {
        Exception exception = new RuntimeException();

        ResponseEntity<String> response = exceptionHandler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("An unexpected error occurred: null", response.getBody());
    }
}