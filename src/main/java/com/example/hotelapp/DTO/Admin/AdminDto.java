package com.example.hotelapp.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDto{
    private String username;
    private String email;
    private String password;

}
