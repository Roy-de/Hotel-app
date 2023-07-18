package com.example.hotelapp.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCredentials {
    private int id;
    private String user_username;
    private String user_password;
    private String User_email;
}
