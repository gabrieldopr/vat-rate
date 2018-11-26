package com.example.countries.entity;

import lombok.Data;

import java.util.List;

@Data
public class Country {
    private String name;
    private String code;
    private String country_code;
    private List<Period> periods;
}
