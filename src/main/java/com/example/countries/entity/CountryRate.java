package com.example.countries.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CountryRate {
    private String country;
    private Float vat;

    public String toString() {
        return country + " - " + vat;
    }
}
