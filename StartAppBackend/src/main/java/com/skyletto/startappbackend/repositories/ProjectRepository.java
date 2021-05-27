package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Location;
import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> getAllByUser(User user);

    List<Project> findAllByLatBetweenAndLngBetween(double v, double v1, double v2, double v3);
}
