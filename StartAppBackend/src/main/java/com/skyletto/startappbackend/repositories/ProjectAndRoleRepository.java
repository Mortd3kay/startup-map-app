package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.ProjectAndRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.PersistenceContext;

@Repository
public interface ProjectAndRoleRepository extends JpaRepository<ProjectAndRole, Long> {
    void deleteAllByProject(Project project);
}
