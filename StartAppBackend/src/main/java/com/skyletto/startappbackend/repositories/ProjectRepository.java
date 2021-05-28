package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Project;
import com.skyletto.startappbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> getAllByUser(User user);

    List<Project> findAllByLatBetweenAndLngBetween(double v, double v1, double v2, double v3);

    @Query(value = "select p.* from projects p where (p.lat between ?1-0.25 and ?1+0.25) and (p.lng between ?2-0.7 and ?2+0.7) and p.id not in (select b.project_id from user_blacklists b where b.user_id = ?3)",nativeQuery = true)
    List<Project> findAllRecommendedProjects(double lat, double lng, long user_id);
}
