package com.example.countries.service;

import com.example.countries.entity.Country;
import com.example.countries.entity.CountryRate;
import com.example.countries.entity.DataResponse;
import com.example.countries.entity.Period;
import com.example.countries.enumerator.RangeType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.countries.enumerator.RangeType.HIGHEST;
import static com.example.countries.enumerator.RangeType.LOWEST;

@Slf4j
@Service
public class CountryService {

    private final RestTemplate restTemplate;

    @Autowired
    public CountryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void printCountriesWithLowestStandardVatRate(Date date, int limit) {
        printCountriesWithStandardVatRate(LOWEST, date, limit);
    }

    public void printCountriesWithHighestStandardVatRate(Date date, int limit) {
        printCountriesWithStandardVatRate(HIGHEST, date, limit);
    }

    private void printCountriesWithStandardVatRate(RangeType rangeType, Date date, int limit) {
        String formattedDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
        log.info("Printing out {} countries with {} standard VAT rates as of {} within the EU", limit, rangeType.name().toLowerCase(), formattedDate);

        List<CountryRate> countryRateList = getCountryRateList(date);

        countryRateList.stream()
                .sorted(rangeType.getComparator())
                .limit(limit)
                .forEach(System.out::println);
    }

    public List<CountryRate> getCountryRateList(Date date) {
        DataResponse dataResponse = getDataResponse();

        List<CountryRate> countryRateList = new ArrayList<>();
        dataResponse.getCountries()
                .forEach(country -> {
                    Period effectivePeriod = getEffectivePeriod(country, date);
                    if (effectivePeriod != null) {
                        String countryName = country.getName();
                        float rate = effectivePeriod.getRates().getStandard();
                        countryRateList.add(new CountryRate(countryName, rate));
                    }
                });

        return countryRateList;
    }

    private DataResponse getDataResponse() {
        return restTemplate.getForObject("http://jsonvat.com/", DataResponse.class);
    }

    private Period getEffectivePeriod(Country country, Date date) {
        return country.getPeriods().stream()
                .sorted((o1, o2) -> o2.getStartDate().compareTo(o1.getStartDate())) // reverse order by effective_from
                .filter(p -> p.getStartDate().equals(date) || p.getStartDate().before(date))
                .findFirst().orElse(null);
    }

}
