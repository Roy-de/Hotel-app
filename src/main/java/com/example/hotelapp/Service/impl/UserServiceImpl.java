package com.example.hotelapp.Service.impl;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Entity.Roles.Role;
import com.example.hotelapp.Entity.User.User;
import com.example.hotelapp.Repository.RoleRepository;
import com.example.hotelapp.Repository.UserRepository;
import com.example.hotelapp.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.*;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }
    @Override
    public User createUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setUsername(userDto.getUsername());
        Role role = roleRepository.findByname("ADMIN");
        if(role == null){
            role = checkRoleExists();
        }
        user.setRoles(Collections.singletonList(role));
       return userRepository.save(user);
    }

    private Role checkRoleExists() {
        Role role = new Role();
        role.setName("ADMIN");
        return roleRepository.save(role);
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

    @Override
    public User findUserByEmail(String email) {
        return null;
    }

    @Override
    public List<UserDto> findAllUsers() {
        return null;
    }

}
