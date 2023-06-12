package com.example.hotelapplication.Service.User;

import com.example.hotelapplication.DAO.UserAccountDao;
import com.example.hotelapplication.Entity.UserEntity;

import java.util.List;

public interface UserAccountService {
    void SaveUser(UserAccountDao userAccountDao);
    UserEntity findUserByUsername(String username);
    List<UserAccountDao> findAllUsers();
}
