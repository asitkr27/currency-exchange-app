package com.example.currencyexchange.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum UserType {
    EMPLOYEE, AFFILIATE, CUSTOMER;

    @JsonCreator
    public static UserType fromString(String value) {
        return Stream.of(UserType.values())
                .filter(type -> type.name().equalsIgnoreCase(value)) // Case-insensitive matching
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid UserType: " + value));
    }

    @JsonValue
    public String toJson() {
        return name(); // Serializes the enum as uppercase
    }
}
