package com.example.currencyexchange.model;

import com.example.currencyexchange.enums.UserType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private UserType userType;  // using the enum
    private int customerTenure;

}