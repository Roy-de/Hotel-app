package com.example.hotelapp.Service.HotelService;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.Repository.impl.HotelRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HotelServiceLayer {
    private final HotelRepositoryImpl hotelRepository;

    public HotelServiceLayer(HotelRepositoryImpl hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    /*--------HOW IT SHOULD WORK----------
        1. List hotels
        * */
/*    public List<HotelDto> list_hotels(){
        return
    }*/
    public List<HotelDto> list_all_hotels(String location){
        location = location.toLowerCase();
        return hotelRepository.list_all_hotels(location);
    }
}
