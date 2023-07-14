package com.example.hotelapp.Controller.MainController;

import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hotels/listing")
@CrossOrigin(origins = {"*"},allowedHeaders = {"*"},methods = {RequestMethod.DELETE,RequestMethod.GET,RequestMethod.POST,RequestMethod.PUT},originPatterns = {"*"})
public class hotelController {
    private final HotelServiceLayer hotelServiceLayer;

    public hotelController(HotelServiceLayer hotelServiceLayer) {
        this.hotelServiceLayer = hotelServiceLayer;
    }

    @GetMapping("/{location}")
    public ResponseEntity<List<HotelObject>> list_hotels(@PathVariable String location){
        try{
            List<HotelObject> hotels = hotelServiceLayer.list_all_hotels(location);
            return ResponseEntity.status(HttpStatus.OK).body(hotels);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/get_hotel/{id}")
    public ResponseEntity<HotelObject> getHotelDetails(@PathVariable int id){
       return ResponseEntity.status(HttpStatus.OK).body(hotelServiceLayer.getHotel(id));
    }
}
