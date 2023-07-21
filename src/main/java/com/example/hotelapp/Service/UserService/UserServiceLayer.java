package com.example.hotelapp.Service.UserService;

import com.example.hotelapp.Config.CustomAuthenticationManager;
import com.example.hotelapp.Controller.Login.ResponseDto;
import com.example.hotelapp.DTO.User.UserDetailsDto;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.DTO.User.UserUpdatedDto;
import com.example.hotelapp.Repository.impl.UserRepositoryImpl;
import com.example.hotelapp.Service.Jwt.JwtService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

/**
* This class is supposed to handle every insertion update and deletion of
* user account.

* By doing so we will be able to manage all connections to the
* database that involves users and their accounts

* We will also implement password encoding here
*/
@Service
@Slf4j
public class UserServiceLayer {

    private final UserRepositoryImpl userRepository;
    private final CustomAuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public UserServiceLayer(UserRepositoryImpl userRepository, CustomAuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public ResponseDto create_user_account(UserDto user){
        ResponseDto responseDto = new ResponseDto();
        try{
            try{
                log.info("Trying to create user");
                userRepository.create_user_account(user);
                log.info("Created User successfully");
              //  mailSender.sendEmail(user.getEmail(),"Welcome to Savvy Sleeps","Welcome"+ user.getName());
            }catch (DataAccessException e){
                responseDto.setStatus(500);
                responseDto.setMessage("Error: "+e);
                return responseDto;
            }
            log.info("Attempting authentication of new user");
            //Authenticate user in order to get access token and log him/ her in
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
            );
            responseDto.setStatus(200);
            responseDto.setMessage(jwtService.generateToken(user.getUsername()));
            log.info("returned jwt token in response");
            return responseDto;
        }catch (Exception e){
            responseDto.setStatus(500);
            responseDto.setMessage("Error: "+e);
            return responseDto;
        }
    }

    public UserDetailsDto get_user(String credentials){
        return userRepository.get_user(credentials);
    }
    public void update_details(UserUpdatedDto userUpdatedDto,String credentials){
        //Get user credentials
        userRepository.update_user(userUpdatedDto,credentials);

    }
}
