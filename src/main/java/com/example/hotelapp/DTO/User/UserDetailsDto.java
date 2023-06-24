package com.example.hotelapp.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDetailsDto {
    private String first_name;
    private String last_name;
    private String email;
    private String phone_no;
    private String username;
}
