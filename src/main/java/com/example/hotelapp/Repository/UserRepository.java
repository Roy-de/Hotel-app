package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findUserByEmail(String email);
}
