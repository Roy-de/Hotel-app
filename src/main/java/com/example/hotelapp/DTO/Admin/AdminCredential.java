package com.example.hotelapp.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdminCredential {
    private String admin_username;
    private String admin_password;
    private String admin_email;
}
