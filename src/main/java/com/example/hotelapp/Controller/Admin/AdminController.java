package com.example.hotelapp.Controller.Admin;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Service.AdminService.AdminServiceLayer;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.security.RolesAllowed;
import javax.ws.rs.NotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin(origins = {"*"},allowedHeaders = {"*"},methods = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT},originPatterns = {"*"},exposedHeaders = {"*"})
public class AdminController {
    private final AdminServiceLayer adminServiceLayer;
    private final HotelServiceLayer hotelServiceLayer;

    public AdminController(AdminServiceLayer adminServiceLayer, HotelServiceLayer hotelServiceLayer) {
        this.adminServiceLayer = adminServiceLayer;
        this.hotelServiceLayer = hotelServiceLayer;
    }

    //Controllers used to create an admin account plus the first listing

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
    public ResponseEntity<?> dashboard(@PathVariable int id, HttpSession session) {
        try {
            List<HotelObject> hotels = adminServiceLayer.get_all_hotels(id);
            session.setAttribute("admin_id",id);
            for(HotelObject hotelObject: hotels){
                List<HotelImagesDto> imagesDtoList = hotelObject.getHotelImagesDto();
                for(HotelImagesDto hotelImagesDto: imagesDtoList){
                    byte[] image = hotelImagesDto.getImage();
                    if(image != null){
                        ByteArrayResource resource = new ByteArrayResource(image);
                        return ResponseEntity.ok().contentType(MediaType.ALL).body(resource);
                    }
                }
            }
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
        if(session.getAttribute("admin_id") ==  null){
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


    @DeleteMapping("/delete/{hotel_id}")
    public ResponseEntity<String> delete_hotel(@PathVariable int hotel_id){
        try{
            String result = adminServiceLayer.delete_hotel(hotel_id);
            return ResponseEntity.ok(result);

        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting hotel");
        }
    }
    @GetMapping("/hello")
    @RolesAllowed("ADMIN")
    public String hello(){
        return "This is secured";
    }
}
