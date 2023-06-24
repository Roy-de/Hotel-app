package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.Config.PasswordChecker;
import com.example.hotelapp.DTO.User.*;
import com.example.hotelapp.Mappers.userRowMapper;
import com.example.hotelapp.Repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Objects;

@Repository
public class UserRepositoryImpl implements UserRepository, PasswordChecker {
    private UserCredentials userCredentials;
    private UserDetailsDto userDetailsDto;
    private UserUpdatedDto userUpdatedDto;
    private UserChangedPassword userChangedPassword;
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_user_account(UserDto userDto) {
        String sql = "CALL public.insert_user_record(?, ?, ?, ?)";
        Object[] params = {
                userDto.getName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail(),
                userDto.getUsername()};
        jdbcTemplate.update(sql,params);
    }

    @Override
    public UserCredentials get_user_credentials(String credentials) {
        String sql = "SELECT user_email,user_password,user_username FROM public.get_user_credentials(?)";
        return jdbcTemplate.queryForObject(sql, new Object[]{credentials}, new userRowMapper());
    }

    @Override
    public UserUpdatedDto update_user(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public UserChangedPassword change_password(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public void delete_user(UserDto userDto) {

    }

    @Override
    public void check_password() {
        if(Objects.equals(userCredentials.getUser_password(), userChangedPassword.getOld_password())){
            if(Objects.equals(userCredentials.getUser_password(),userChangedPassword.getNew_password())){
                String message = "password identical";
            }
            else{
                userCredentials.setUser_password(passwordEncoder.encode(userChangedPassword.getNew_password()));
            }
        }else{
            String message = "Old password is incorrect";
        }
    }
}
