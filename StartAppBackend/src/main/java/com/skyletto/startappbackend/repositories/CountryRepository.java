package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CountryRepository  extends JpaRepository<Country, Integer> {
    @Query("SELECT c from Country c where c.name = :name")
    Country findCountryByName(@Param("name")String name);
}
