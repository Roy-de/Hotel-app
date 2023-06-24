package com.example.hotelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangedPassword {
    private String old_password;
    private String new_password;
    private String confirm_password;
}
