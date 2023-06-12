package com.example.hotelapplication.Service.User.impl;

import com.example.hotelapplication.DAO.UserAccountDao;
import com.example.hotelapplication.Entity.UserEntity;
import com.example.hotelapplication.Repository.UserRepository;
import com.example.hotelapplication.Service.User.UserAccountService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

public class UserAccountServiceImpl implements UserAccountService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserAccountServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void SaveUser(UserAccountDao userAccountDao) {
        UserEntity user = new UserEntity();
        user.setName(userAccountDao.getName());
        user.setEmail(userAccountDao.getEmail());
        user.setUsername(userAccountDao.getUsername());
        user.setPassword(passwordEncoder.encode(userAccountDao.getPassword()));
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserAccountDao> findAllUsers() {
        List<UserEntity> users = userRepository.findAll();
        return users.stream().map((user) ->convertEntitytoDao(user))
                .collect(Collectors.toList());
    }
    public UserAccountDao convertEntitytoDao(UserEntity user){
        UserAccountDao userAccountDao = new UserAccountDao();
        userAccountDao.setName(user.getName());
        userAccountDao.setUsername(user.getUsername());
        userAccountDao.setEmail(user.getEmail());
        return userAccountDao;
    }
}
