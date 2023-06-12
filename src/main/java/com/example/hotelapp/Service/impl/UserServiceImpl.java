package com.example.hotelapp.Service.impl;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.User.User;
import com.example.hotelapp.Repository.UserRepository;
import com.example.hotelapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /* private PasswordEncoder passwordEncoder;*/
    @Override
    public User createUser(User user) {
        user.setEmail(user.getEmail());
        user.setName(user.getName());
        user.setPassword(user.getPassword());
        user.setUsername(user.getUsername());

       return userRepository.save(user);
    }

    @Override
    public User updateUser(String username, UserDto userDto) {
        User user = userRepository.findByUsername(username);
        if(user != null){
            user.setName(userDto.getName());
            user.setUsername(userDto.getUsername());
            user.setEmail(userDto.getEmail());
            user.setPassword(userDto.getPassword());
            return userRepository.save(user);
        }
        return null;
    }

    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User getUsername(String username) {
        return userRepository.findByUsername(username);
    }

}
