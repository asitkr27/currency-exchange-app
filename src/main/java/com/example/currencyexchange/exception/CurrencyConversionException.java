package com.example.currencyexchange.exception;

import lombok.Getter;
import org.springframework.http.HttpStatusCode;

@Getter
public class CurrencyConversionException extends RuntimeException {
    private final HttpStatusCode status;

    public CurrencyConversionException(HttpStatusCode status, String message) {
        super(message);
        this.status = status;
    }
}