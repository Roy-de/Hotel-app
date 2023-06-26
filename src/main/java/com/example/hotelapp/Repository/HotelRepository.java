package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HotelRepository {
    //---------CREATE HOTEL---------------
    //Create hotel
    void create_hotel(HotelDto hotelDto);
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
    List<HotelDto> list_all_hotels(HotelDto hotelDto,HotelImagesDto hotelImagesDto);
    //list Hotel services
    HotelServicesDto list_hotel_services(HotelServicesDto hotelServicesDto);
    //---------------DELETE HOTEL-----------
    void delete_hotel(HotelDto hotelDto);
}
