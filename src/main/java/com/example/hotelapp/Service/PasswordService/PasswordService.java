package com.example.hotelapp.Service.PasswordService;

import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final ChangePasswordServiceImpl changePasswordService;

    public PasswordService(ChangePasswordServiceImpl changePasswordService) {
        this.changePasswordService = changePasswordService;
    }
    //Use this service to change password
    //Send password change request for admin and get change password from email
}