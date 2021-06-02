package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByEmail(String email);

    User findUserByEmailAndPassword(String email, String pass);

    @Query(value = "select * from users u cross join users_locations l on u.id = l.user_id " +
            "where (l.lat between ?1-0.25 and ?1+0.25) and (l.lng between ?2-0.7 and ?2+0.7) and u.id not in " +
            "(select user_id from project_blacklists where project_id = ?3 union select user_id from projects where id = ?3)",nativeQuery = true)
    List<User> findAllRecommendedUsers(double lat, double lng, long project_id);
}
