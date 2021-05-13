package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> getAllByUser(User user);

    Long deleteProjectById(Long id);
}
