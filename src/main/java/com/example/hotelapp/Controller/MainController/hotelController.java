package com.example.hotelapp.Controller.MainController;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.Repository.impl.HotelRepositoryImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/")
public class hotelController {
    private final HotelRepositoryImpl hotelRepository;

    public hotelController(HotelRepositoryImpl hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    @GetMapping("/list_hotel/{location}")
    public ResponseEntity<List<HotelDto>> list_hotels(@PathVariable String location){
        return ResponseEntity.status(HttpStatus.OK).body(hotelRepository.list_all_hotels(location));
    }
}
