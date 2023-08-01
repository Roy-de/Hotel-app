package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.Config.JwtAuthFilter;
import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {"*"},allowedHeaders = {"*"},methods = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT},originPatterns = {"*"},exposedHeaders = {"*"})
public class AdminController {
    private final AdminServiceLayer adminServiceLayer;
    private final HotelServiceLayer hotelServiceLayer;
    private final JwtAuthFilter authFilter;

    public AdminController(AdminServiceLayer adminServiceLayer, HotelServiceLayer hotelServiceLayer, JwtAuthFilter authFilter) {
        this.adminServiceLayer = adminServiceLayer;
        this.hotelServiceLayer = hotelServiceLayer;
        this.authFilter = authFilter;
    }

    /**
     * We will be doing everything systematically
     * 1.Log in and go to the dashboard
     * @param session session to store username
     * @return  all the hotels the admin owns
     */
    @GetMapping("/dashboard")
    public ResponseEntity<?> dashboard(HttpSession session) {
        try {
            String username = authFilter.getUsername();
            log.info("Got username: {}",username);
            List<HotelObject> hotels = adminServiceLayer.get_all_hotels(username);
            List<HotelImagesDto> images = new ArrayList<>();
            session.setAttribute("admin_username",username);
            return ResponseEntity.ok(hotels);
        } catch (NotFoundException e) {
            // Return 404 Not Found if the admin or hotels are not found
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Admin or hotels not found");
        } catch (Exception e) {
            // Return 500 Internal Server Error for other generic exceptions
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    /** UPDATE ADMIN DETAILS, HOTEL DETAILS, HOTEL IMAGES, HOTEL SERVICES */
    @PutMapping("/{hotel_id}/images")
    public ResponseEntity<String> insertImages(@PathVariable("hotel_id") int hotelId,
                                               @RequestParam(value = "images") List<MultipartFile> images,
                                               @RequestParam(value = "description",required = false) List<String> descriptions) {
        try {
            if (images == null || images.isEmpty()) {
                return ResponseEntity.badRequest().body("No images provided");
            }


            List<HotelImagesDto> hotelImagesDtoList = hotelServiceLayer.images(images, descriptions);

            hotelServiceLayer.insert_image(hotelId, hotelImagesDtoList);
            return ResponseEntity.ok("Success");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PutMapping("/update_details")
    public ResponseEntity<?> update_details(@RequestBody AdminDetailsDto adminDetailsDto){
        String username = authFilter.getUsername();
        try{
            String response = adminServiceLayer.edit_details(adminDetailsDto,username);
            return ResponseEntity.ok("Successfully edited details"+ response);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error editing details: "+e);
        }
    }
    /* Admin login request mapping and post mapping

     */
    /* --------------------ADMIN DASHBOARD ---------------*/
    //This is what should be displayed when the admin logs in

    //update hotel details

    @PostMapping("/create_hotel")
    public ResponseEntity<String> create_hotel(@RequestBody HotelObject hotelObject){
        try{
            //Get the username from the threadLocal
            //Get the admin id using the username so that we can insert a new hotel using it as the foreign key
            int admin_id = adminServiceLayer.get_admin_id(authFilter.getUsername());
            log.info("Admin id {}",admin_id);
            hotelServiceLayer.create_hotel(admin_id, hotelObject.getHotelDto(), hotelObject.getHotelServicesDto(), hotelObject.getHotelImagesDto());
            return ResponseEntity.ok("Success");
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e);
        }
    }
    //select hotel to edit and put it in a session
    @GetMapping("/edit_hotel/{id}")
    public ResponseEntity<?> select_hotel(@PathVariable int id,HttpSession session){
        /*By doing this, we first ensure that the admin or hotel owner has logged in and is
        * in the dashboard section. We get the admin id after logging in, and then he can select
        * the hotel he/she wants to edit. we will capture the hotel id now, store it in a session
        * so that we can be able to edit the services, images and details.
        * */
        try{
            if (session.getAttribute("admin_id") == null) {
               return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("You cant edit the hotel");
            }
            session.setAttribute("hotel_id",id);
            HotelObject hotelObject =  hotelServiceLayer.getHotel(id);
            return ResponseEntity.status(HttpStatus.OK).body(hotelObject);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " +e);
        }
    }
    //function to check for admin id and hotel id in session
    public String check_session(HttpSession session){
        if(session.getAttribute("admin_username") ==  null){
            return "You are not the admin of this hotel";
        }else if(session.getAttribute("hotel_id") == null){
            return "You cannot edit this hotel";
        }else {
            return null;
        }
    }

    //edit hotel services
    @PutMapping("/edit_hotel/edit_services")
    public ResponseEntity<?> edit_hotel_services(@RequestBody HotelServicesDto hotelServicesDto,HttpSession session){
        try{
            String result = check_session(session);
            if(result.equals("You are not the admin of this hotel")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            } else if (result.equals("You cannot edit this hotel")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }else{
                int id = (int) session.getAttribute("hotel_id");
                hotelServiceLayer.update_service(hotelServicesDto,id);
                return ResponseEntity.status(HttpStatus.OK).body("Edited the services");
            }
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error:" +e);
        }
    }
    //Edit hotel name or details
    @PutMapping("/edit_hotel/edit_hotel")
    public ResponseEntity<?> edit_hotel_details(@RequestBody HotelDto hotelDto, HttpSession session){
        try{
            String result = check_session(session);
            if(result.equals("You are not the admin of this hotel")){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            } else if (result.equals("You cannot edit this hotel")) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
            }else{
                int id = (int) session.getAttribute("hotel_id");
                hotelServiceLayer.update_hotel_detail(hotelDto,id);
                return ResponseEntity.ok("Success");
            }
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+e);
        }
    }
    //Edit hotel images
    @PutMapping("/edit_hotel/edit_images")
    public ResponseEntity<?> update_images(@RequestParam("images") List<MultipartFile> images, @RequestParam(value = "description", required = false) List<String> descriptions) {
        String message = null;
        try{
            List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();
            int id = adminServiceLayer.get_admin_id(authFilter.getUsername());
            for (int i = 0; i < images.size(); i++) {
                MultipartFile image = images.get(i);
                String description = descriptions.get(i); // Get the description for the corresponding image (if available)

                // Create a new HotelImagesDto object and add it to the list
                HotelImagesDto hotelImageDto = new HotelImagesDto();
                //hotelImageDto.setImage(image.getBytes()); // Assuming the HotelImagesDto has a property 'image' to store the image bytes
                hotelImageDto.setDescription(description); // Assuming the HotelImagesDto has a property 'description' to store the description
                hotelImagesDtoList.add(hotelImageDto);
            }
            message =  hotelServiceLayer.update_images(hotelImagesDtoList,id);
            return ResponseEntity.ok(message);
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+message);
        }

    }
    @DeleteMapping("/delete/image")
    public ResponseEntity<?> delete_image(@RequestBody List<Integer> ids){
        try{
            hotelServiceLayer.delete_image(ids);
            return ResponseEntity.ok("Success");
        }catch (DataAccessException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: "+e.getMessage());
        }
    }
    @DeleteMapping("/delete")
    public ResponseEntity<String> delete_hotel(@RequestBody int hotel_id){
        try{
            String username = authFilter.getUsername();
            String result = adminServiceLayer.delete_hotel(hotel_id,username);
            return ResponseEntity.ok(result);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting hotel");
        }
    }
    /*
    From here we will add the capabilities of the admin to view bookings and comments
     */
}
