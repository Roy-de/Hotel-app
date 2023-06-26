package com.example.hotelapp.Service.AdminService;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.Repository.impl.AdminRepositoryImpl;
import org.springframework.stereotype.Service;

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
    public String create_account_and_hotel(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto){
        //Take in admin details and check if such an account exists
        try{
            if()
        }catch
    }
    public boolean isAdminExist(String credentials){
        return adminRepository.search_for_username(credentials);
    }
}
