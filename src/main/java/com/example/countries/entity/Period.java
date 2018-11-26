package com.example.countries.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class Period {
    @JsonProperty("effective_from")
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Europe/Berlin")
    private Date startDate;
    private Rate rates;
}
