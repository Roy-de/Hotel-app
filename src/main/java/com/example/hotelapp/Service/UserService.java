package com.example.hotelapp.Service;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;

public interface UserService {
    //Create
    User createUser(User user);
    //Update
    User updateUser(String username,UserDto userDto);
    //Delete
    void deleteUser(Long id);
    //Read
    User getUsername(String username);
}
