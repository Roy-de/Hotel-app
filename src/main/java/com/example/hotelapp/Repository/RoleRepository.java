package com.example.hotelapp.Repository;

import com.example.hotelapp.Entity.Roles.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByname(String username);

}
