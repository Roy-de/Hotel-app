package com.example.hotelapp.Controller.User;

import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.ExceptionHandlers.Exception.UserNotFoundException;
import com.example.hotelapp.Service.UserService.UserServiceLayer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserServiceLayer userServiceLayer;
    @Autowired
    public UserController(UserServiceLayer userServiceLayer) {
        this.userServiceLayer = userServiceLayer;
    }
    //Post mapping to post user details when creating an account
    @PostMapping("/create")
    public ResponseEntity<String> create_user(@RequestBody @Validated UserDto user){
        try{
            userServiceLayer.create_user_account(user);
            ResponseEntity.status(HttpStatus.CREATED).body("Account created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create account: "+e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Account created");
    }
    @GetMapping("/{credentials}")
    public UserCredentials get_user_credentials(@PathVariable String credentials) throws UserNotFoundException {
        try{
            return userServiceLayer.get_user_credentials(credentials);
        }
        catch (Exception e){
            ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e);
        }
        return userServiceLayer.get_user_credentials(credentials);
    }

}
