package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.City;
import com.skyletto.startappbackend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Integer> {
    //@Query(nativeQuery = true, value = "select * from cities c where c.country_id in (select country_id from countries where countries.name = ?1)")
    List<City> findCitiesByCountry(Country country);
}
