package com.example.currencyexchange.service;

public interface CurrencyExchangeService {
    Double getExchangeRate(String fromCurrency, String toCurrency);
}
