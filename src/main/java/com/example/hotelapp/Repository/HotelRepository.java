package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository {
    //---------CREATE HOTEL---------------
    //Create hotel
    int create_hotel(HotelDto hotelDto,HotelServicesDto hotelServicesDto);
    //----------INSERT IMAGES -------------
    void insert_images(List<byte[]> images, List<String> descriptions,int Hotel_id);
    /*
    ---------------- EDIT HOTEL -----------
     */
    //Edit hotel services
    void edit_hotel_services(HotelServicesDto hotelServicesDto);
    //Edit hotel images
    void edit_hotel_images(HotelImagesDto hotelImagesDto);
    //edit hotel details
    void edit_hotel_details(HotelDto hotelDto);
    /*
    ---------------VIEW HOTELS--------------
     */
    //List all hotels
    List<HotelObject> list_all_hotels(String location);
    //Get hotel object
    HotelObject get_hotel_by_id(int id);
}
