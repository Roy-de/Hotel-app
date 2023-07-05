package com.example.hotelapp.Service.AdminService;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Repository.impl.AdminRepositoryImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceLayer {
    private final AdminRepositoryImpl adminRepository;

    public AdminServiceLayer(AdminRepositoryImpl adminRepository) {
        this.adminRepository = adminRepository;
    }

    /*--------HOW IT SHOULD WORK----------
        1.Get hotel details based on the admin account
        2.Store the details of the hotel in a hotel DAO
        3.We also need to load the hotel services too
            -Have a DAO for services
            -Have a DAO for the hotel itself
        4.Change the details of the hotel
        * */
/*------------CREATE ACCOUNT PROCEDURE HERE---------------
* 1. Get admin details into admin dto
* 2. Get Admin Details into admin details dto
* 3. Get hotel details into hotel dto*/
    //admin service to check if username and service exists
    public String check_admin_details_validity(AdminDto adminDto){
        if(adminRepository.search_for_username(adminDto.getUsername())){
            return "Username is already taken";
        } else if (adminRepository.search_for_username(adminDto.getEmail())) {
            return "Email is already registered";
        }else
            return "proceed to create account";
    }

    public void create_account_and_hotel(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto, List<HotelImagesDto> hotelImagesDtoList){
        //Create account now
        adminRepository.create_account(adminDto,adminDetailsDto,hotelDto,hotelImagesDtoList);
    }
    public boolean isAdminExist(String credentials){
        return adminRepository.search_for_username(credentials);
    }
    public List<HotelObject> get_all_hotels(int hotel_id){
        return adminRepository.get_all_hotels(hotel_id);
    }
    public String delete_hotel(int id){
        return adminRepository.delete_hotel(id);
    }
}
