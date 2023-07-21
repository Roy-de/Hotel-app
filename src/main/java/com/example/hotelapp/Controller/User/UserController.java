package com.example.hotelapp.Controller.User;

import com.example.hotelapp.Config.JwtAuthFilter;
import com.example.hotelapp.DTO.Booking.BookingDto;
import com.example.hotelapp.DTO.Comments.CommentDto;
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
public class UserController {

    private final UserServiceLayer userServiceLayer;
    private final JwtAuthFilter authFilter;
    @Autowired
    public UserController(UserServiceLayer userServiceLayer, JwtAuthFilter authFilter) {
        this.userServiceLayer = userServiceLayer;
        this.authFilter = authFilter;
    }
    //Post mapping to post user details when creating an account

    @GetMapping("/get_details")
    public ResponseEntity<?> get_user(HttpSession session){
        //Gets user details and returns them
        try{
            UserDetailsDto userDetailsDto= userServiceLayer.get_user(authFilter.getUsername());
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
    //Comment
    @PostMapping("/comment")
    public ResponseEntity<?> create_comment(@RequestBody CommentDto commentDto){
        //create comment
        try{
            userServiceLayer.create_comment(commentDto,authFilter.getUsername());
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+e.getMessage());
        }
    }
    //Booking
    @PostMapping("/booking")
    public ResponseEntity<?> create_booking(@RequestBody BookingDto bookingDto){
        try{
            userServiceLayer.create_booking(bookingDto,authFilter.getUsername());
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+e.getMessage());
        }
    }
}
