package com.example.hotelapp.Service;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;

public interface UserService {
    //Create
    User createUser(UserDto userDto);
    //Update
    User updateUser(String username,UserDto userDto);
    //Delete
    void deleteUser(String username);
    //Read
    User getUsername(String username);
}
