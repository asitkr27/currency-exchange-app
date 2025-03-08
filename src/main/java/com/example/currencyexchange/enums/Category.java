package com.example.currencyexchange.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum Category {
    GROCERY, ELECTRONICS, CLOTHING, FURNITURE, BEAUTY, TOYS, SPORTS, AUTOMOTIVE;

    @JsonCreator
    public static Category fromString(String value) {
        return Stream.of(Category.values())
                .filter(category -> category.name().equalsIgnoreCase(value)) // Case-insensitive matching
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Category: " + value));
    }

    @JsonValue
    public String toJson() {
        return name(); // Serializes the enum as uppercase
    }
}
