package com.skyletto.startappbackend.repositories;

import com.skyletto.startappbackend.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, String> {
    Role findRoleByName(String name);
}
