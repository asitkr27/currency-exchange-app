package com.example.currencyexchange.validation;

import com.example.currencyexchange.model.Bill;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class BillValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return Bill.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Bill bill = (Bill) target;

        // Custom validation logic if necessary
        if (bill.getItems() == null || bill.getItems().isEmpty()) {
            errors.rejectValue("items", "items.empty", "Items list cannot be empty");
        }

        if (bill.getTotalAmount() <= 0) {
            errors.rejectValue("totalAmount", "totalAmount.invalid", "Total amount must be greater than zero");
        }

        if (bill.getOriginalCurrency() == null || bill.getOriginalCurrency().isEmpty()) {
            errors.rejectValue("originalCurrency", "originalCurrency.empty", "Original currency cannot be empty");
        }

        if (bill.getTargetCurrency() == null || bill.getTargetCurrency().isEmpty()) {
            errors.rejectValue("targetCurrency", "targetCurrency.empty", "Target currency cannot be empty");
        }

        if (bill.getUser() == null) {
            errors.rejectValue("user", "user.null", "User cannot be null");
        }
    }
}
