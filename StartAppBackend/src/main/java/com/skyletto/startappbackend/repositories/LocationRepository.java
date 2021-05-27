package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {
    List<Location> findAllByLatBetweenAndLngBetween(double latL, double latR, double lngL, double lngR);
}
