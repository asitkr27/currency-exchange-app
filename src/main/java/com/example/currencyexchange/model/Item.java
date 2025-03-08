package com.example.currencyexchange.model;
import com.example.currencyexchange.enums.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Item {
    private String name;
    private Category category;  //  using the enum
    private double price;
    private int quantity;
}