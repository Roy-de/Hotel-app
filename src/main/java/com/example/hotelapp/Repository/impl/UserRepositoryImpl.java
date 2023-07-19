package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.ChangedPassword;
import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.DTO.User.UserDetailsDto;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.DTO.User.UserUpdatedDto;
import com.example.hotelapp.Mappers.UserDetailsMapper;
import com.example.hotelapp.Repository.UserRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl implements UserRepository{
    private UserCredentials userCredentials;
    private UserDetailsDto userDetailsDto;
    private UserUpdatedDto userUpdatedDto;
    private ChangedPassword changedPassword;
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
    public UserDetailsDto get_user(String credentials) {
        String sql = "SELECT * FROM public.user_details WHERE user_details.user_account_id IN (SELECT id FROM public.user_account WHERE user_account.username = ? OR user_account.email = ?)";
        return jdbcTemplate.queryForObject(sql,new UserDetailsMapper(),credentials,credentials);
    }

    @Override
    public void update_user(UserUpdatedDto userUpdatedDto, String credentials) {
        String sql = "UPDATE public.user_details SET first_name = ?,last_name = ?,phone_no = ? where username = ? or email = ?";
        Object[] params = {
                userUpdatedDto.getFirst_name(),
                userUpdatedDto.getLast_name(),
                userUpdatedDto.getPhone_no(),
                credentials,
                credentials
        };

        jdbcTemplate.update(sql, params);
    }

    @Override
    public ChangedPassword change_password(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public void delete_user(UserDto userDto) {
        String sql = "DELETE FROM public.user_account WHERE email = ?";
        jdbcTemplate.update(sql,userDto.getUsername());
    }
}
