package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegionRepository  extends JpaRepository<Region, Integer> {
}
