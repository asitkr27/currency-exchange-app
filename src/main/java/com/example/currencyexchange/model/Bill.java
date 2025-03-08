package com.example.currencyexchange.model;

import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Bill {
    private List<Item> items;
    private double totalAmount;
    private String originalCurrency;
    private String targetCurrency;
    private User user;
}