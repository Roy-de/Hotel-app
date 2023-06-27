package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminServiceLayer adminServiceLayer;

    public AdminController(AdminServiceLayer adminServiceLayer) {
        this.adminServiceLayer = adminServiceLayer;
    }
    @GetMapping("/get/{credentials}")
    public ResponseEntity<Boolean> get_admin(@PathVariable String credentials){
        boolean isValid = adminServiceLayer.isAdminExist(credentials);
        return ResponseEntity.ok(isValid);
    }
    //Controllers used to create an admin account plus the first listing
    @PostMapping("/create_account")
    public ResponseEntity<String> create_account(@RequestBody AdminDto adminDto){
        //Get user details then check if the two objects exist
        String validityMessage = adminServiceLayer.check_admin_details_validity(adminDto);
        if(validityMessage.equals("Username is already taken")){
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validityMessage);
        } else if (validityMessage.equals("Email is already registered")) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validityMessage);
        }
        return ResponseEntity.status(HttpStatus.OK).body("redirect:/add_details");
    }
    @PostMapping("/add_details")
    public ResponseEntity<String> add_details(@RequestBody AdminDetailsDto adminDetailsDto){
        try{
            String message = adminServiceLayer.create_admin_details(adminDetailsDto);
            return ResponseEntity.status(HttpStatus.OK).body("Saved successfully. " + message + "redirect:/host");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/host")
    public ResponseEntity<String> host(@RequestBody HotelDto hotelDto){
        try{
            String message = adminServiceLayer.create_hotel(hotelDto);
            return ResponseEntity.status(HttpStatus.CREATED).body("Hotel created" + message + "redirect:/final_step");
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Error: " + e.getMessage());
        }
    }
    @PostMapping("/Final_step")
    public ResponseEntity<String> final_step(AdminDto adminDto,AdminDetailsDto adminDetailsDto, HotelDto hotelDto){
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(adminServiceLayer.create_account_and_hotel(adminDto,adminDetailsDto,hotelDto));
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encountered:"+ e);
        }
    }
    //Done
    /*--------------- UPDATE ADMIN DETAILS, HOTEL DETAILS, HOTEL IMAGES, HOTEL SERVICES -------------------*/
    @PutMapping("/update_details")
    public ResponseEntity<?> update_details(){
        return null;
    }
    /* Admin login request mapping and post mapping

     */
    /* --------------------ADMIN DASHBOARD ---------------*/
    //This is what should be displayed when the admin logs in
    @GetMapping("/dashboard")
    public ResponseEntity<List<HotelObject>> dashboard(HotelObject hotelObject){
        try{
            return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonList(hotelObject));
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
