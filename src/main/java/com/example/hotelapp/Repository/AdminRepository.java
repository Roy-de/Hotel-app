package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository {
    //Method to create admin account
    void create_account(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto, List<HotelImagesDto> hotelImagesDtoList);
    //method to search for username and or email
    boolean search_for_username(String credentials);
    List<HotelObject> get_all_hotels(String username);
    String delete_hotel(int id);
    String edit_Details(AdminDetailsDto adminDetailsDto,String username);
    Integer get_admin_id(String username);
}
