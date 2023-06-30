package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository {
    //Method to create admin account
    void create_account(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto);
    //method to get admin details
    AdminDto get_Admin_credentials(AdminDto adminDto);
    //Method to update hotel details
    //method to search for username and or email
    boolean search_for_username(String credentials);
    List<HotelObject> get_all_hotels(int admin_id);
}
