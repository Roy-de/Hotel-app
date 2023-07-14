package com.example.hotelapp.Controller.Login;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.UserService.UserServiceLayer;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
@CrossOrigin(origins = {"*"},allowedHeaders = {"*"},methods = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT},originPatterns = {"*"})
public class LoginController {
    /**
     * This part we will put all the necessary classes that we will use
     */
    private final UserServiceLayer userServiceLayer;
    private final AdminServiceLayer adminServiceLayer;

    public LoginController(UserServiceLayer userServiceLayer, AdminServiceLayer adminServiceLayer) {
        this.userServiceLayer = userServiceLayer;
        this.adminServiceLayer = adminServiceLayer;
    }

    @PostMapping("/admin")
    public ResponseEntity<?> admin_login(@RequestBody AdminCredential adminCredential) {
       return null;
    }
    @PostMapping("/user")
    public ResponseEntity<?>user_login(@RequestBody UserCredentials credentials){
        return null;
    }

    /**
     * This part is used to create user accounts
     * @param user
     * The <code>create_user()</code> Takes in User details i.e username, full name
     * ,email and password into the data transfer object called <code>UserDto</code>
     * @return String "Account created successfully"
     */
    @PostMapping("/create_user")
    public ResponseEntity<String> create_user(@RequestBody @Validated UserDto user){
        try{
            userServiceLayer.create_user_account(user);
            ResponseEntity.status(HttpStatus.CREATED).body("Account created");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create account: "+e);
        }
        return ResponseEntity.status(HttpStatus.OK).body("Account created");
    }

    /**Here we will perform a multistep procedure in order to register a new hotel administrator
     * The first step is to Get his details into the <code>AdminDto</code> object
     * @param adminDto  get the username, email and password
     * @param session   this will just store the data into a session
     * @return  account creates successfully if username and email are not pre-registered
     */
    @PostMapping("/create_account")
    public ResponseEntity<String> createAccount(@RequestBody AdminDto adminDto, HttpSession session) {
        // Get user details then check if the two objects exist
        String validityMessage = adminServiceLayer.check_admin_details_validity(adminDto);
        if (validityMessage.equals("Username is already taken")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validityMessage);
        } else if (validityMessage.equals("Email is already registered")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(validityMessage);
        }
        session.setAttribute("adminDto", adminDto);
        return ResponseEntity.status(HttpStatus.OK).body("Create Account Successful");
    }

    @PostMapping("/add_details")
    public ResponseEntity<String> addDetails(@RequestBody AdminDetailsDto adminDetailsDto, HttpSession session) {
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        if (adminDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin details not found. Please start the signup process again.");
        }
        try {
            session.setAttribute("adminDetailsDto", adminDetailsDto);
            return ResponseEntity.status(HttpStatus.OK).body("Add Details Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/host")
    public ResponseEntity<String> host(
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions,
            @RequestParam String name,
            @RequestParam String location,
            @RequestParam String description,
            @RequestParam double pricing,
            @RequestParam int no_of_beds,
            @RequestParam int rooms_available,
            @RequestParam double longitude,
            @RequestParam double latitude,
            @RequestParam String place,
            HttpSession session) {
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        AdminDetailsDto adminDetailsDto = (AdminDetailsDto) session.getAttribute("adminDetailsDto");
        if (adminDto == null || adminDetailsDto == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Admin details or hotel information not found. Please start the signup process again.");
        }
        try {
            HotelDto hotelDto = new HotelDto();
            hotelDto.setName(name);
            hotelDto.setLocation(location);
            hotelDto.setDescription(description);
            hotelDto.setPricing(pricing);
            hotelDto.setNo_of_beds(no_of_beds);
            hotelDto.setRooms_available(rooms_available);
            hotelDto.setLongitude(longitude);
            hotelDto.setLatitude(latitude);
            hotelDto.setPlace(place);

            List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();

            if (images != null && descriptions != null && images.size() == descriptions.size()) {
                hotelImagesDtoList = images(images, descriptions);
            } else {
                // Handle the case where images and descriptions are null or have different sizes
                HotelImagesDto placeholderDto = new HotelImagesDto();
                placeholderDto.setImage(null);
                placeholderDto.setDescription("No images available");
                hotelImagesDtoList.add(placeholderDto);
            }

            session.setAttribute("hotelImages", hotelImagesDtoList);
            session.setAttribute("hotelDto", hotelDto);

            return ResponseEntity.status(HttpStatus.OK).body("Host Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/Final_step")
    public ResponseEntity<?> finalStep(HttpSession session) {
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        AdminDetailsDto adminDetailsDto = (AdminDetailsDto) session.getAttribute("adminDetailsDto");
        HotelDto hotelDto = (HotelDto) session.getAttribute("hotelDto");
        List<HotelImagesDto> hotelImagesDtoList = (List<HotelImagesDto>) session.getAttribute("hotelImages");

        if (adminDto == null || adminDetailsDto == null || hotelDto == null) {
            return ResponseEntity.badRequest().body("Incomplete signup data. Please start the signup process again.");
        }

        try {
            adminServiceLayer.create_account_and_hotel(adminDto, adminDetailsDto, hotelDto,hotelImagesDtoList);
            session.invalidate();
            return ResponseEntity.status(HttpStatus.OK).body("Account created successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error encountered: " + e.getMessage());
        }
    }
    private List<HotelImagesDto> images(@RequestParam("images") List<MultipartFile> images,
                                        @RequestParam(value = "description", required = false) List<String> descriptions) throws IOException {
        List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String description = (descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null;
            byte[] imageBytes = image.getBytes();

            HotelImagesDto hotelImagesDto = new HotelImagesDto(imageBytes, description);
            hotelImagesDtoList.add(hotelImagesDto);
        }
        return hotelImagesDtoList;
    }
}
