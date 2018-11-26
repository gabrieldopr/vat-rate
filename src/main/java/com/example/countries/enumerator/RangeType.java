package com.example.countries.enumerator;

import com.example.countries.entity.CountryRate;

import java.util.Comparator;

public enum RangeType {

    LOWEST(Comparator.comparing(CountryRate::getVat)),
    HIGHEST((o1, o2) -> o2.getVat().compareTo(o1.getVat()));

    private final Comparator<? super CountryRate> comparator;

    RangeType(Comparator<? super CountryRate> comparator) {
        this.comparator = comparator;
    }

    public Comparator<? super CountryRate> getComparator() {
        return this.comparator;
    }
}
