package com.example.hotelapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotEmpty(message = "Fill in your name")
    private String name;
    @Email
    @NotEmpty(message = "Fill in email")
    private String email;
    @NotEmpty
    private String password;
}
