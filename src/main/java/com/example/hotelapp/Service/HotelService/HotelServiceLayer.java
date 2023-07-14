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
    public List<HotelObject> list_all_hotels(String location){
        return hotelRepository.list_all_hotels(location.toLowerCase());
    }
    //List hotel and services offered and images it has
    public HotelObject getHotel(int hotel_id){
        return hotelRepository.get_hotel_by_id(hotel_id);
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
    public void create_hotel(HotelDto hotelDto,HotelServicesDto hotelServicesDto,List<HotelImagesDto> hotelImagesDtoList){
        //Create the hotel
        int id = hotelRepository.create_hotel(hotelDto,hotelServicesDto);
        //associate hotel images to hotel
        insert_image(id,hotelImagesDtoList);
    }
    public void update_service(HotelServicesDto hotelServicesDto, int hotel_id){
        hotelRepository.edit_hotel_services(hotelServicesDto,hotel_id);
    }
    public void update_hotel_detail(HotelDto hotelDto,int hotel_id){
        hotelRepository.edit_hotel_details(hotelDto,hotel_id);
    }
}
