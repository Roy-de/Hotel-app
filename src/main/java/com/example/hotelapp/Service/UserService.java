package com.example.hotelapp.Service;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;

import java.util.List;

public interface UserService {
    //Create
    User createUser(UserDto userDto);
    //Update
    User updateUser(String username,UserDto userDto);
    //Delete
    void deleteUser(Long id);
    //Read
    User getUsername(String username);
    User findUserByEmail(String email);
    List<UserDto> findAllUsers();
}
