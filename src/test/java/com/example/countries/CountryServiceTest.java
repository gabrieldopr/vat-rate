package com.example.countries;

import com.example.countries.entity.CountryRate;
import com.example.countries.service.CountryService;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    private final Date today = new Date();

    @Test
    public void testPrintCountryList() {
        final int LIMIT = 3;

        countryService.printCountriesWithLowestStandardVatRate(today, LIMIT);
        countryService.printCountriesWithHighestStandardVatRate(today, LIMIT);
    }

    @Test
    public void testStandardVatRatePerEffectivePeriod() {
        String country = "Romania";

        assertVat(country, 2013, 24.0f);
        assertVat(country, 2016, 20.0f);
        assertVat(country, 2019, 19.0f);
    }

    @SneakyThrows
    private void assertVat(String country, int year, float expectedStandardVatRate) {
        String stringDate = "January 1, " + year;

        Date date = new SimpleDateFormat("MMMM d, yyyy", Locale.getDefault()).parse(stringDate);

        List<CountryRate> countryRateList = countryService.getCountryRateList(date);

        CountryRate countryRate = countryRateList.stream()
                .filter(p -> p.getCountry().equals(country))
                .findAny().orElse(null);

        assertNotNull(String.format("It was not possible to locate standard VAT rate for %s on %s", country, stringDate), countryRate);
        float actualVat = countryRate.getVat();

        assertEquals(String.format("Unexpected standard VAT rate for %s on %s", country, stringDate), expectedStandardVatRate, actualVat, 0);
    }

}