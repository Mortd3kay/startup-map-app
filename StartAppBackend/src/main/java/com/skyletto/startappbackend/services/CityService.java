package com.skyletto.startappbackend.services;

import com.skyletto.startappbackend.entities.City;
import com.skyletto.startappbackend.entities.Country;
import com.skyletto.startappbackend.repositories.CityRepository;
import com.skyletto.startappbackend.repositories.CountryRepository;
import com.skyletto.startappbackend.repositories.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class CityService {
    private CountryRepository countryRepository;
    private RegionRepository regionRepository;
    private CityRepository cityRepository;

    @Autowired
    public void setCountryRepository(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    @Autowired
    public void setRegionRepository(RegionRepository regionRepository) {
        this.regionRepository = regionRepository;
    }
    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<Country> getAllCountries(){
        return countryRepository.findAll(Sort.by("name").ascending());
    }

    public List<City> getCitiesByCountry(String country){
        return cityRepository.findCitiesByCountry_Name(country);
    }
}
