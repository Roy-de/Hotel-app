package com.example.hotelapp.Service.PasswordService;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ChangePasswordServiceImpl implements ChangePasswordService{
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();


}
