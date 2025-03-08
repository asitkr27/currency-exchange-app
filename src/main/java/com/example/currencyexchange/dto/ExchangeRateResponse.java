package com.example.currencyexchange.dto;

import lombok.Data;
import java.util.Map;

@Data
public class ExchangeRateResponse {
    private String base_code; // Base currency (e.g., "USD")
    private Map<String, Double> conversion_rates; // Key: currency code, Value: exchange rate
}
