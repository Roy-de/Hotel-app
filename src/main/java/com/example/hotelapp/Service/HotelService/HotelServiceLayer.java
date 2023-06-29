package com.example.hotelapp.Service.HotelService;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Repository.impl.HotelRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    //List hotel and services offered and images it has
    public HotelObject getHotel(int hotel_id){
        HotelDto hotelDto = new HotelDto();
        List<HotelImagesDto> hotelImagesDtos = new ArrayList<>();
        HotelServicesDto hotelServicesDto = new HotelServicesDto();
        return new HotelObject(hotelDto,hotelImagesDtos,hotelServicesDto);
    }
    public void insert_image(int Hotel_id,List<HotelImagesDto> hotelImagesDtoList){
        List<byte[]> images = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        for(HotelImagesDto hotelImagesDto: hotelImagesDtoList){
            byte[] imageFile = hotelImagesDto.getImage();
            String description = hotelImagesDto.getDescription();
            images.add(imageFile);
            descriptions.add(description);
        }
        hotelRepository.insert_images(images,descriptions,Hotel_id);
    }
}
