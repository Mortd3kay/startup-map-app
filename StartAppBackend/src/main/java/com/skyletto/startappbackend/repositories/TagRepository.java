package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
    List<Tag> findTagsByNameStartsWith(String str);
    List<Tag> findTagsByNameContains(String str);
}
