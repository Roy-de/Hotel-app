package com.example.hotelapp.Controller.User;

import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.DTO.User.UserDetailsDto;
import com.example.hotelapp.DTO.User.UserUpdatedDto;
import com.example.hotelapp.Service.UserService.UserServiceLayer;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = {"*"},allowedHeaders = {"*"},methods = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT},originPatterns = {"*"})
public class UserController {

    private final UserServiceLayer userServiceLayer;
    @Autowired
    public UserController(UserServiceLayer userServiceLayer) {
        this.userServiceLayer = userServiceLayer;
    }
    //Post mapping to post user details when creating an account

    @GetMapping("/login")
    public ResponseEntity<?> get_user_credentials(@RequestBody UserCredentials userCredentials) {
        try{
            String credentials;
            if(userCredentials.getUser_email() == null)
                credentials = userCredentials.getUser_username();
            else
                credentials = userCredentials.getUser_email();
            userServiceLayer.get_user_credentials(credentials);
            return ResponseEntity.ok("Done");
        }
        catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e);
        }

    }
    @GetMapping("/get_details/{credentials}")
    public ResponseEntity<?> get_user(@PathVariable String credentials, HttpSession session){
        //Gets user details and returns them
        try{
            UserDetailsDto userDetailsDto= userServiceLayer.get_user(credentials);
            session.setAttribute("user_details",userDetailsDto);
            return  ResponseEntity.status(HttpStatus.OK).body(userDetailsDto);
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User does not exist");
        }
    }
    @PutMapping("/update_user")
    public ResponseEntity<String> update_user(@RequestBody UserUpdatedDto userUpdatedDto, HttpSession session){
        try{
            UserDetailsDto userDetailsDto = (UserDetailsDto) session.getAttribute("user_details");
            if(userDetailsDto == null){
                return ResponseEntity.status(HttpStatus.CONFLICT).body("You are trying to update a user without getting the details");
            }
            userServiceLayer.update_details(userUpdatedDto,userDetailsDto.getUsername());
            return ResponseEntity.ok("Successfully updated details");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" + e);
        }
    }
    @GetMapping("/hello")
    public String hello(){
        return "This is user secured endpoint";
    }
}
