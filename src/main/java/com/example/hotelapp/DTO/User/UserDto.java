package com.example.hotelapp.DTO.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @NotEmpty(message = "Username should not be empty")
    private String username;
    @NotEmpty(message = "Fill in your name")
    private String name;
    @Email
    @NotEmpty(message = "Fill in email")
    private String email;
    @NotEmpty(message = "password must be filled")
    @Size(min = 8,message = "Password should be of 8 characters")
    private String password;
}
