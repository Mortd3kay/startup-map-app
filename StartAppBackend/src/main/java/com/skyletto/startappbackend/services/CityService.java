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
    public CityService(CountryRepository countryRepository, RegionRepository regionRepository, CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.regionRepository = regionRepository;
        this.cityRepository = cityRepository;
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll(Sort.by("name").ascending());
    }

    public List<City> getCitiesByCountry(int id) {
        return cityRepository.findCitiesByCountry_Id(id);
    }

    public List<City> getAllCities(){
        return cityRepository.findAll();
    }
}
