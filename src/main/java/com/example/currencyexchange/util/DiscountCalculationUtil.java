package com.example.currencyexchange.util;

import com.example.currencyexchange.enums.Category;
import com.example.currencyexchange.enums.UserType;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.model.User;
import org.springframework.stereotype.Service;

@Service
public class DiscountCalculationUtil {

    public static double applyDiscounts(Bill bill) {
        User user = bill.getUser();
        double discount = 0.0;

        // Calculate total amount dynamically from item prices and quantities
        double totalAmount = bill.getItems().stream()
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Calculate total non-grocery amount (considering quantity)
        double nonGroceryAmount = bill.getItems().stream()
                .filter(item -> item.getCategory() != Category.GROCERY) // Use enum comparison
                .mapToDouble(item -> item.getPrice() * item.getQuantity())
                .sum();

        // Apply only one of the percentage-based discounts
        if (user.getUserType() == UserType.EMPLOYEE) {
            discount = 0.30 * nonGroceryAmount;
        } else if (user.getUserType() == UserType.AFFILIATE) {
            discount = 0.10 * nonGroceryAmount;
        } else if (user.getUserType() == UserType.CUSTOMER && user.getCustomerTenure() > 2) {
            discount = 0.05 * nonGroceryAmount;
        }

        // Apply percentage discount first and then calculate $5 per $100 discount
        double discountedAmount = totalAmount - discount;

        // Apply $5 discount for every $100 spent (after percentage discount)
        discount += ((int) (discountedAmount / 100)) * 5;

        return discount;
    }

}
