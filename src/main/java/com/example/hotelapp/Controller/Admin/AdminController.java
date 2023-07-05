package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
public class AdminController {
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
            @RequestParam(value = "images") List<MultipartFile> images,
            @RequestParam(value = "descriptions",required = false) List<String> descriptions,
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Admin details or hotel information not found. Please start the signup process again.");
        }
        try {
            HotelDto hotelDto =  new HotelDto();
            hotelDto.setName(name);
            hotelDto.setLocation(location);
            hotelDto.setDescription(description);
            hotelDto.setPricing(pricing);
            hotelDto.setNo_of_beds(no_of_beds);
            hotelDto.setRooms_available(rooms_available);
            hotelDto.setLongitude(longitude);
            hotelDto.setLatitude(latitude);
            hotelDto.setPlace(place);
            List<HotelImagesDto> hotelImagesDtoList = images(images,descriptions);
            session.setAttribute("hotelImages",hotelImagesDtoList);
            session.setAttribute("hotelDto", hotelDto);
            return ResponseEntity.status(HttpStatus.OK).body("Host Successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/Final_step")
    public ResponseEntity<String> finalStep(HttpSession session) {
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


            List<HotelImagesDto> hotelImagesDtoList = images(images, descriptions);

            hotelServiceLayer.insert_image(hotelId, hotelImagesDtoList);
            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
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
    //update hotel details

    //delete hotel
    @PostMapping("/CreateHotel")
    public ResponseEntity<String> create_hotel(@RequestBody HotelObject hotelObject){
        try{
            hotelServiceLayer.create_hotel(hotelObject.getHotelDto(),hotelObject.getHotelServicesDto(),hotelObject.getHotelImagesDto());
            return ResponseEntity.ok("Success");
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e);
        }
    }

    @DeleteMapping("/delete/{hotel_id}")
    public void delete_hotel(@PathVariable int hotel_id, HttpServletResponse response) throws IOException {
        JsonMapper mapper =new JsonMapper();
        Map<String, String> map= new HashMap<>();
        try{
            adminServiceLayer.delete_hotel(hotel_id);
            map.put("response", "success");
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(),map);

        }catch (Exception e){
            map.put("response", String.valueOf(e));
            response.setContentType("application/json");
            mapper.writeValue(response.getOutputStream(),map);
        }
    }
}
