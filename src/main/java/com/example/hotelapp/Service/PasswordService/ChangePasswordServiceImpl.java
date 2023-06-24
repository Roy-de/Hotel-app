package com.example.hotelapp.Service.PasswordService;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import com.example.hotelapp.DTO.ChangedPassword;
import com.example.hotelapp.DTO.User.UserCredentials;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class ChangePasswordServiceImpl implements ChangePasswordService{
    private UserCredentials userCredentials;
    private AdminCredential adminCredential;
    private ChangedPassword changedPassword;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Override
    public void Change_user_password() {
        if (Objects.equals(userCredentials.getUser_password(), changedPassword.getOld_password())) {
            if (Objects.equals(userCredentials.getUser_password(), changedPassword.getNew_password())) {
                String message = "password identical";
            } else {
                userCredentials.setUser_password(passwordEncoder.encode(changedPassword.getNew_password()));
                String message = "Password changed Successfully";
            }
        } else {
            String message = "Old password is incorrect";
        }
    }

    @Override
    public void change_admin_password() {
        if (Objects.equals(adminCredential.getAdmin_password(), changedPassword.getOld_password())) {
            if (Objects.equals(adminCredential.getAdmin_password(), changedPassword.getNew_password())) {
                String message = "password identical";
            } else {
                adminCredential.setAdmin_password(passwordEncoder.encode(changedPassword.getNew_password()));
                String message = "Password changed Successfully";
            }
        } else {
            String message = "Old password is incorrect";
        }
    }
}
