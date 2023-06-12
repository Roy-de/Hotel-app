package com.example.hotelapp.Service.impl;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;
import com.example.hotelapp.Repository.UserRepository;
import com.example.hotelapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
   /* private PasswordEncoder passwordEncoder;*/
    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setPassword(userDto.getPassword());
        user.setUsername(userDto.getUsername());

       return userRepository.save(user);
    }

}
