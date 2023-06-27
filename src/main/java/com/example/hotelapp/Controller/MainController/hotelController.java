package com.example.hotelapp.Controller.MainController;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hotels/listing")
public class hotelController {
    private final HotelServiceLayer hotelServiceLayer;

    public hotelController(HotelServiceLayer hotelServiceLayer) {
        this.hotelServiceLayer = hotelServiceLayer;
    }

    @GetMapping("/{location}")
    public ResponseEntity<List<HotelDto>> list_hotels(@PathVariable String location){
        try{
            List<HotelDto> hotels = hotelServiceLayer.list_all_hotels(location);
            return ResponseEntity.status(HttpStatus.OK).body(hotels);
        }catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("/{id}")
    public ResponseEntity<HotelObject> getHotelDetails(@PathVariable int id){
       return ResponseEntity.status(HttpStatus.OK).body(hotelServiceLayer.getHotel(id));
    }
}
