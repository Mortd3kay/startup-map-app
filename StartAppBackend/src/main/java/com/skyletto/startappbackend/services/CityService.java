package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.City;
import com.skyletto.startappbackend.entities.Country;
import com.skyletto.startappbackend.repositories.CityRepository;
import com.skyletto.startappbackend.repositories.CountryRepository;
import com.skyletto.startappbackend.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityService {
    private CountryRepository countryRepository;
    private RegionRepository regionRepository;
    private CityRepository cityRepository;

    @Autowired
    public CityService(CountryRepository countryRepository, RegionRepository regionRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
    }

    public List<Country> getAllCountries(){
        return countryRepository.findAll(Sort.by("name").ascending());
    }

    public List<City> getCitiesByCountry(String country){
        Country c = countryRepository.findCountryByName(country);
        List<City> cities= cityRepository.findCitiesByCountry(c);
        return cities;
    }
}
