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
    int create_hotel(int admin_id,HotelDto hotelDto,HotelServicesDto hotelServicesDto);
    //----------INSERT IMAGES -------------
    void insert_images(List<byte[]> images, List<String> descriptions,int Hotel_id);
    /*
    ---------------- EDIT HOTEL -----------
     */
    //Edit hotel services
    void edit_hotel_services(HotelServicesDto hotelServicesDto,int hotel_id);
    //Edit hotel images
    void edit_hotel_images(List<HotelImagesDto> hotelImagesDto,int hotel_id);
    //edit hotel details
    void edit_hotel_details(HotelDto hotelDto,int hotel_id);
    void delete_image(List<Integer> ids);
    /*
    ---------------VIEW HOTELS--------------
     */
    //List all hotels
    List<HotelObject> list_all_hotels(String location);
    //Get hotel object
    HotelObject get_hotel_by_id(int id);
}
