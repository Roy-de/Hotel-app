package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Admin.AdminDto;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository {
    //Method to create admin account
    void create_admin_account(AdminDto adminDto);
    //method to get admin details
    AdminDto get_Admin_credentials(AdminDto adminDto);
    //Method to update hotel details

}
