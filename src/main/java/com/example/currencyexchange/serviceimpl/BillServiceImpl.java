package com.example.currencyexchange.serviceimpl;

import com.example.currencyexchange.exception.CurrencyConversionException;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.service.BillService;
import com.example.currencyexchange.util.DiscountCalculationUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class BillServiceImpl implements BillService {
    private CurrencyExchangeServiceImpl currencyExchangeService;

    @Override
    public double calculateNetAmount(Bill bill) {
        // Dynamically calculate total amount instead of relying on bill.getTotalAmount()
        double totalAmount = bill.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        double netAmount = Math.max(totalAmount - discount, 0); // Prevents negative values

        Double exchangeRate = currencyExchangeService.getExchangeRate(
                bill.getOriginalCurrency(), bill.getTargetCurrency());

        if (exchangeRate == null || exchangeRate <= 0) {
            throw new CurrencyConversionException(HttpStatus.BAD_REQUEST,
                    "Invalid exchange rate received for " + bill.getOriginalCurrency() + " to " + bill.getTargetCurrency());
        }

        return netAmount * exchangeRate;
    }

}
