package com.example.hotelapp.Service.UserService;

import com.example.hotelapp.DTO.User.*;
import com.example.hotelapp.Repository.impl.UserRepositoryImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/*
* This class is supposed to handle every insertion update and deletion of
* user account.
*
* By doing so we will be able to manage all connections to the
* database that involves users and their accounts
*
* We will also implement password encoding here
*/
@Service
public class UserServiceLayer {
    private final UserRepositoryImpl userRepository;
    private UserDetailsDto userDetailsDto;
    @Autowired
    public UserServiceLayer(UserRepositoryImpl userRepository) {
        this.userRepository = userRepository;
    }
    public void create_user_account(UserDto userDto){
        userRepository.create_user_account(userDto);
    }
    public UserCredentials get_user_credentials(String credentials){
       return userRepository.get_user_credentials(credentials);
    }
/*    public void update_details(String credentials){
        //Get user credentials
        get_user_credentials(credentials);
        userRepository.update_user()

    }*/
}
