package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/admin")
public class AdminController {
    //Logger logger = LoggerFactory.getLogger(LoggerFactory.class);
    private final AdminServiceLayer adminServiceLayer;
    private final HotelServiceLayer hotelServiceLayer;

    public AdminController(AdminServiceLayer adminServiceLayer, HotelServiceLayer hotelServiceLayer) {
        this.adminServiceLayer = adminServiceLayer;
        this.hotelServiceLayer = hotelServiceLayer;
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
        return ResponseEntity.status(HttpStatus.FOUND).header(HttpHeaders.LOCATION,"/add_details").build();
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
    @PutMapping("/{hotel_id}/images")
    public ResponseEntity<String> insertImages(@PathVariable("hotel_id") int hotelId,
                                               @RequestParam(value = "images") List<MultipartFile> images,
                                               @RequestParam(value = "description",required = false) List<String> descriptions) {
        try {
            if (images == null || images.isEmpty()) {
                return ResponseEntity.badRequest().body("No images provided");
            }

            List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();

            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                String description = (descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null;
                byte[] imageBytes = image.getBytes();

                HotelImagesDto hotelImagesDto = new HotelImagesDto(imageBytes, description);
                hotelImagesDtoList.add(hotelImagesDto);
            }

            hotelServiceLayer.insert_image(hotelId, hotelImagesDtoList);
            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update_details")
    public ResponseEntity<?> update_details(){
        return null;
    }
    /* Admin login request mapping and post mapping

     */
    /* --------------------ADMIN DASHBOARD ---------------*/
    //This is what should be displayed when the admin logs in
    @GetMapping("/dashboard/{id}")
    public ResponseEntity<?> dashboard(@PathVariable int id) {
        try {
            List<HotelObject> hotels = adminServiceLayer.get_all_hotels(id);
            return ResponseEntity.ok(hotels);
        } catch (NotFoundException e) {
            // Return 404 Not Found if the admin or hotels are not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin or hotels not found");
        } catch (Exception e) {
            // Return 500 Internal Server Error for other generic exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


    @PostMapping("/CreateHotel")
    public ResponseEntity<String> create_hotel(@RequestBody HotelDto hotelDto,
                                               @RequestBody List<HotelImagesDto> hotelImagesDtoList,
                                               @RequestBody HotelServicesDto hotelServicesDto){
        try{
            hotelServiceLayer.create_hotel(hotelDto,hotelServicesDto,hotelImagesDtoList);
            return ResponseEntity.ok("Success");
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e);
        }
    }
}
