package com.example.hotelapp.DTO.Admin;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminDetailsDto {
    private String first_name;
    private String last_name;
    private String email;
    private String phone_no;
    private String alt_phone_no;
    private String alt_email;
}
