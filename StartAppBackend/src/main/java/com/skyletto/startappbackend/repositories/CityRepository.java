package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    List<City> findCitiesByCountry_Name(String country);
}
