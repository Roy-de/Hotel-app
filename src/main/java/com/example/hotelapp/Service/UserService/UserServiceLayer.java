package com.example.hotelapp.Service.UserService;

import com.example.hotelapp.DTO.User.UserDetailsDto;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.DTO.User.UserUpdatedDto;
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
    public void get_user_credentials(String credentials){
        userRepository.get_user_credentials(credentials);
    }
    public UserDetailsDto get_user(String credentials){
        return userRepository.get_user(credentials);
    }
    public void update_details(UserUpdatedDto userUpdatedDto,String credentials){
        //Get user credentials
        userRepository.update_user(userUpdatedDto,credentials);

    }
    public void inactivate_account(){
        //userRepository.delete_user();
    }
}
