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
import com.example.hotelapp.Service.ImageService.ImageServiceLayer;
import com.example.hotelapp.Service.Jwt.JwtService;
import com.example.hotelapp.Service.UserService.UserServiceLayer;
import jakarta.servlet.http.HttpSession;
import lombok.SneakyThrows;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@Slf4j
public class LoginController {
    public static final String USERNAME_ERROR = "Error: org.springframework.dao.DuplicateKeyException: PreparedStatementCallback; SQL [CALL public.insert_user_record(?, ?, ?, ?)]; ERROR: Username is already taken.\n" +
            "  Where: PL/pgSQL function insert_user_record(character varying,character varying,character varying,character varying) line 9 at RAISE";
    public static final String EMAIL_ERROR = "Error: org.springframework.dao.DuplicateKeyException: PreparedStatementCallback; SQL [CALL public.insert_user_record(?, ?, ?, ?)]; ERROR: Email is already registered.\n" +
            "  Where: PL/pgSQL function insert_user_record(character varying,character varying,character varying,character varying) line 14 at RAISE";
    /**
 * This part we will put all the necessary classes that we will use
 */
    private final ConvertEmailToUsername convertEmailToUsername;
    private final CustomAuthenticationManager authenticationManager;
    private final ImageServiceLayer imageServiceLayer;
    private final UserServiceLayer userServiceLayer;
    private final AdminServiceLayer adminServiceLayer;
    private final JwtService jwtService;
    @Autowired
    public LoginController(ConvertEmailToUsername convertEmailToUsername, CustomAuthenticationManager authenticationManager, ImageServiceLayer imageServiceLayer, UserServiceLayer userServiceLayer, AdminServiceLayer adminServiceLayer, JwtService jwtService) {
        this.convertEmailToUsername = convertEmailToUsername;
        this.authenticationManager = authenticationManager;
        this.imageServiceLayer = imageServiceLayer;
        this.userServiceLayer = userServiceLayer;
        this.adminServiceLayer = adminServiceLayer;
        this.jwtService = jwtService;
    }
    @PostMapping("/login/admin")
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
    @PostMapping("/login/user")
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password:"+e.getMessage());
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
    @PostMapping("/create_user_account")
    public ResponseEntity<?> create_user_account(@RequestBody @Validated UserDto user){
        try{
            ResponseDto responseDto = userServiceLayer.create_user_account(user);
           if(responseDto.getMessage().equals(USERNAME_ERROR) || responseDto.getMessage().equals(EMAIL_ERROR)){
               responseDto.setMessage("Username or email already used");
               return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
           }else{
               responseDto.setMessage("Success");
               return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
           }
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
    @PostMapping(value = "/create_admin_account",produces = "application/json")
    public ResponseEntity<?> createAccount(@RequestBody AdminDto adminDto, HttpSession session) {
        ResponseDto responseDto = new ResponseDto();
        // Get user details then check if the two objects exist
        String validityMessage = adminServiceLayer.check_admin_details_validity(adminDto);
        if (validityMessage.equals("Username is already taken")) {
            responseDto.setStatus(400);
            responseDto.setMessage("Username is already taken");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        } else if (validityMessage.equals("Email is already registered")) {
            responseDto.setStatus(400);
            responseDto.setMessage("Email is already registered");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
        session.setAttribute("adminDto", adminDto);
        responseDto.setStatus(200);
        responseDto.setMessage("Create Account Successful");
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**Get the administrator of the hotel details
     *
     * @param adminDetailsDto get the details of admin. that is the
     *                       <p> 1. First and last name</p>
     *                        <p>2. Phone number (primary)</p>
     *                        <p>3. Alternative phone number and email (Optional)</p>
     * @param session   just store the data in a session to be used later
     */
    @PostMapping("/add_details")
    public ResponseEntity<ResponseDto> addDetails(@RequestBody AdminDetailsDto adminDetailsDto, HttpSession session) {
        ResponseDto responseDto = new ResponseDto();
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        if (adminDto == null) {
            responseDto.setStatus(400);
            responseDto.setMessage("Admin details not found. Please start the signup process again.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseDto);
        }
        try {
            session.setAttribute("adminDetailsDto", adminDetailsDto);
            responseDto.setStatus(200);
            responseDto.setMessage("Add Details Successful");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            responseDto.setStatus(500);
            responseDto.setMessage(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
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
    public ResponseEntity<ResponseDto> host(
            @RequestParam(value = "images", required = false) List<MultipartFile> images,
            @RequestParam(value = "descriptions", required = false) List<String> descriptions, @RequestParam String name,
            @RequestParam String location, @RequestParam String description, @RequestParam double pricing,
            @RequestParam int no_of_beds, @RequestParam int rooms_available, @RequestParam double longitude,
            @RequestParam double latitude, @RequestParam String place, HttpSession session) {
        ResponseDto responseDto = new ResponseDto();
        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        AdminDetailsDto adminDetailsDto = (AdminDetailsDto) session.getAttribute("adminDetailsDto");
        if (adminDto == null || adminDetailsDto == null) {
            responseDto.setStatus(400);
            responseDto.setMessage("Admin details or hotel information not found. Please start the signup process again.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(responseDto);
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

            if (images != null) {
                for(int i = 0;i < images.size();i++){
                    MultipartFile image = images.get(i);
                    HotelImagesDto imagesDto = imageServiceLayer.uploadImage(image);
                    log.info("Controller level");
                    log.info("Public ID: {}",imagesDto.getPublic_id());
                    log.info("Secure URL: {}",imagesDto.getImageUrl());
                    if (descriptions != null && i < descriptions.size()) {
                        String ImageDescription = descriptions.get(i);
                        imagesDto.setDescription(ImageDescription);
                    }
                    hotelImagesDtoList.add(imagesDto);
                }
            } else {
                // Handle the case where images and descriptions are null or have different sizes
                HotelImagesDto placeholderDto = new HotelImagesDto();
                placeholderDto.setPublic_id(null);
                placeholderDto.setImageUrl(null);
                log.info("Controller level");
                log.info("Public ID: {}",placeholderDto.getPublic_id());
                log.info("Secure URL: {}",placeholderDto.getImageUrl());
                placeholderDto.setDescription("No images available");
                hotelImagesDtoList.add(placeholderDto);
            }

            session.setAttribute("hotelImages", hotelImagesDtoList);
            session.setAttribute("hotelDto", hotelDto);
            responseDto.setStatus(200);
            responseDto.setMessage("Host Successful");
            return ResponseEntity.status(HttpStatus.OK).body(responseDto);
        } catch (Exception e) {
            responseDto.setStatus(500);
            responseDto.setMessage(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }

    /**This part is just a button to complete the registration
     *
     * @param session gets all other sessions and create the hotel account plus the administrator
     * @return  <code>String</code> - account created successfully
     */
    @SneakyThrows
    @PostMapping("/admin/final_step")
    @SuppressWarnings("Unchecked cast: 'java.lang.Object' to 'java.util.List<com.example.hotelapp.DTO.Hotel.HotelImagesDto>'")
    public ResponseEntity<ResponseDto> finalStep(HttpSession session) {
        ResponseDto responseDto = new ResponseDto();

        AdminDto adminDto = (AdminDto) session.getAttribute("adminDto");
        AdminDetailsDto adminDetailsDto = (AdminDetailsDto) session.getAttribute("adminDetailsDto");
        HotelDto hotelDto = (HotelDto) session.getAttribute("hotelDto");

        List<HotelImagesDto> hotelImagesDtoList = (List<HotelImagesDto>) session.getAttribute("hotelImages");

        if (adminDto == null || adminDetailsDto == null || hotelDto == null) {
            responseDto.setStatus(400);
            responseDto.setMessage("Incomplete signup data. Please start the signup process again.");
            return ResponseEntity.badRequest().body(responseDto);
        }

        try {
            ResponseDto response = adminServiceLayer.create_account_and_hotel(adminDto, adminDetailsDto, hotelDto,hotelImagesDtoList);
            session.invalidate();
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            responseDto.setStatus(500);
            responseDto.setMessage(e.getLocalizedMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDto);
        }
    }
}
