package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.UserLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<UserLocation, Long> {
}
