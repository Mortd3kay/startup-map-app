package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<UserLocation, Long> {
    List<UserLocation> findAllByLatBetweenAndLngBetween(double latL, double latR, double lngL, double lngR);
}
