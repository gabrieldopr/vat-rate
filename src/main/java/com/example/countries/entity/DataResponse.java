package com.example.countries.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class DataResponse {
    private String details;
    private String version;
    @JsonProperty("rates")
    private List<Country> countries;
}
