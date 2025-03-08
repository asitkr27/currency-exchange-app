package com.example.currencyexchange.serviceimpl;

import com.example.currencyexchange.dto.ExchangeRateResponse;
import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.service.CurrencyExchangeService;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;


@AllArgsConstructor
@Component
@EnableCaching
public class CurrencyExchangeServiceImpl implements CurrencyExchangeService {
    private static final String API_URL = "https://v6.exchangerate-api.com/v6/";
    private static final String API_KEY = "dce31f10462e2ee38f0b1ba5";
    private final RestTemplate restTemplate = new RestTemplate();

    // https://v6.exchangerate-api.com/v6/dce31f10462e2ee38f0b1ba5/latest/INR
    @Override
    @Cacheable(value = "exchangeRates", key = "#fromCurrency + '-' + #toCurrency")
    public Double getExchangeRate(String fromCurrency, String toCurrency) {
        try {
            String url = API_URL + API_KEY + "/latest/" + fromCurrency;

            ExchangeRateResponse response = restTemplate.getForObject(url, ExchangeRateResponse.class);

            if (response == null || response.getBase_code() == null || response.getConversion_rates() == null) {
                throw new CurrencyConversionException(HttpStatus.BAD_REQUEST, "Invalid response from exchange rate API");
            }


            if (!response.getConversion_rates().containsKey(toCurrency)) {
                throw new CurrencyConversionException(HttpStatus.BAD_REQUEST, "Invalid currency conversion request: " + fromCurrency + " to " + toCurrency);
            }

            Double exchangeRate = response.getConversion_rates().get(toCurrency);
            return exchangeRate;
        } catch (HttpClientErrorException e) {
            throw new CurrencyConversionException(e.getStatusCode(), "Invalid currency code: " + fromCurrency);
        } catch (HttpServerErrorException e) {
            throw new CurrencyConversionException(e.getStatusCode(), "Server error while fetching exchange rate: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            throw new CurrencyConversionException(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected error occurred while fetching exchange rate");
        }
    }

}
