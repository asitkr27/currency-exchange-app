package com.example.currencyexchange.util;

import com.example.currencyexchange.enums.Category;
import com.example.currencyexchange.enums.UserType;
import com.example.currencyexchange.model.Bill;
import com.example.currencyexchange.model.Item;
import com.example.currencyexchange.model.User;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DiscountCalculationUtilTest {

    @Test
    void testApplyDiscounts_Employee() {
        User user = new User(UserType.EMPLOYEE, 3);
        List<Item> items = List.of(
                new Item("Laptop",  Category.ELECTRONICS,1000.0, 2), // Non-grocery
                new Item("Apple",  Category.GROCERY,5.0, 1) // Grocery
        );
        Bill bill = new Bill(items, 2005.0, "USD", "INR", user);

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        assertEquals(670.0, discount, "Employee should get 30% off on non-grocery and $5 per $100 discount");
    }

    @Test
    void testApplyDiscounts_Affiliate() {
        User user = new User(UserType.AFFILIATE, 1);
        List<Item> items = List.of(
                new Item("TV", Category.ELECTRONICS, 500.0, 1), // Non-grocery
                new Item("Rice", Category.GROCERY, 50.0, 2) // Grocery
        );
        Bill bill = new Bill(items, 600.0, "USD", "INR", user);

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        assertEquals(75.0, discount, "Affiliate should get 10% off non-grocery + $5 per $100 discount");
    }

    @Test
    void testApplyDiscounts_LoyalCustomer() {
        User user = new User(UserType.CUSTOMER, 3); // More than 2 years
        List<Item> items = List.of(
                new Item("Fridge",  Category.ELECTRONICS,800.0, 1)
        );
        Bill bill = new Bill(items, 800.0, "USD", "INR", user);

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        assertEquals(75.0, discount, "Loyal customer should get 5% off + $5 per $100 discount");
    }

    @Test
    void testApplyDiscounts_OnlyGroceries() {
        User user = new User(UserType.EMPLOYEE, 5);
        List<Item> items = List.of(
                new Item("Milk",  Category.GROCERY,50.0, 2),
                new Item("Bread",  Category.GROCERY, 20.0,1)
        );
        Bill bill = new Bill(items, 120.0, "USD", "INR", user);

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        assertEquals(5.0, discount, "Only $5 per $100 discount should apply to groceries");
    }

    @Test
    void testApplyDiscounts_NoEligibleDiscount() {
        User user = new User(UserType.CUSTOMER, 1); // Less than 2 years, no discount
        List<Item> items = List.of(
                new Item("Washing Machine",  Category.ELECTRONICS, 700.0,1)
        );
        Bill bill = new Bill(items, 700.0, "USD", "INR", user);

        double discount = DiscountCalculationUtil.applyDiscounts(bill);
        assertEquals(35.0, discount, "Only $5 per $100 discount should apply, no percentage discount");
    }
}
