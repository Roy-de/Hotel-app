package com.example.hotelapp.Controller.Login;

import com.example.hotelapp.Config.CustomAuthenticationManager;
import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.CredentialsDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.ConvertEmailToUsername;
import com.example.hotelapp.Service.Jwt.JwtService;
import com.example.hotelapp.Service.UserService.UserServiceLayer;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/login")
@Slf4j
public class LoginController {
    private final ConvertEmailToUsername convertEmailToUsername;
    private final CustomAuthenticationManager authenticationManager;
    /**
     * This part we will put all the necessary classes that we will use
     */
    private final UserServiceLayer userServiceLayer;
    private final AdminServiceLayer adminServiceLayer;
    private final JwtService jwtService;
    @Autowired
    public LoginController(ConvertEmailToUsername convertEmailToUsername, CustomAuthenticationManager authenticationManager, UserServiceLayer userServiceLayer, AdminServiceLayer adminServiceLayer, JwtService jwtService) {
        this.convertEmailToUsername = convertEmailToUsername;
        this.authenticationManager = authenticationManager;
        this.userServiceLayer = userServiceLayer;
        this.adminServiceLayer = adminServiceLayer;
        this.jwtService = jwtService;
    }

    @PostMapping("/admin")
    public ResponseEntity<?> admin_login(@RequestBody CredentialsDto credentials) {
       try{
           authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
           );
            String username = convertEmailToUsername.adminEmail(credentials.getUsername());
           ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(200);
            responseDto.setMessage(jwtService.generateToken(username));
           return ResponseEntity.ok(responseDto);
       }catch (InternalAuthenticationServiceException e){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
       }catch (BadCredentialsException e){
           return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
       }
    }
    @PostMapping("/user")
    public ResponseEntity<?>user_login(@RequestBody CredentialsDto credentials){
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword())
            );
            String username = convertEmailToUsername.userEmail(credentials.getUsername());
            ResponseDto responseDto = new ResponseDto();
            responseDto.setStatus(200);
            responseDto.setMessage(jwtService.generateToken(username));
            return ResponseEntity.ok(responseDto);
        }catch (InternalAuthenticationServiceException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }catch (BadCredentialsException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Bad credentials");
        }
    }

    /**
     * This part is used to create user accounts
     * @param user
     * The <code>create_user_account()</code> Takes in User details i.e. username, full name
     * ,email and password into the data transfer object called <code>UserDto</code>
     * @return String "Account created successfully"
     */
    @PostMapping("/create_user")
    public ResponseEntity<?> create_user_account(@RequestBody @Validated UserDto user){
        try{
            ResponseDto responseDto = userServiceLayer.create_user_account(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
        }catch (DuplicateKeyException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Could not create account: Username is already taken" + e);
        }
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

    /**Get the administrator of the hotel details
     *
     * @param adminDetailsDto get the details of admin. that is the
     *                        1. First and last name
     *                        2. Phone number (primary)
     *                        3. Alternative phone number and email (Optional)
     * @param session   just store the data in a session to be used later
     */
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

    /**This is where it gets complicated. An admin can decide not to add images
     * as he is hosting his/her hotel.So we will get each detail as a request parameter
     *
     * @param images    images of the hotel
     * @param descriptions  description about the images (Optional)
     * @param name  name of the hotel
     * @param location  Where is the hotel located (County)
     * @param description   About the hotel
     * @param pricing   pricing per room assuming all rooms are identical
     * @param no_of_beds    number of beds per room
     * @param rooms_available   number of rooms in total in the hotel
     * @param longitude   where is the hotel located on the map
     * @param latitude   where is the hotel located on the map
     * @param place Region within the county
     * @param session   same as other sessions
     */
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

    /**This part is just a button to complete the registration
     *
     * @param session gets all other sessions and create the hotel account plus the administrator
     * @return  <code>String</code> - account created successfully
     */
    @PostMapping("/final_step")
    public ResponseEntity<?> finalStep(HttpSession session) {
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        AdminDetailsDto adminDetailsDto = (AdminDetailsDto) session.getAttribute("adminDetailsDto");
        HotelDto hotelDto = (HotelDto) session.getAttribute("hotelDto");
        @SuppressWarnings("Unchecked cast: 'java.lang.Object' to 'java.util.List<com.example.hotelapp.DTO.Hotel.HotelImagesDto>'")
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
