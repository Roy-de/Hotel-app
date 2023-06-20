package com.example.hotelapp.Service.UserService;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Mappers.userDetailsMapper;
import com.example.hotelapp.Mappers.userRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/*
* This class is supposed to handle every insertion update and deletion of
* user account.
*
* By doing so we will be able to manage all connections to the
* database that involves users and their accounts
*
* We will also implement password encoding here
*/
@Service
public class UserJDBCService {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcOperations jdbcOperations;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    public UserJDBCService(JdbcTemplate jdbcTemplate, JdbcOperations jdbcOperations) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcOperations = jdbcOperations;
    }

    //Method to create user account with password encoding
    public void create_user(UserDto userDto){
        String sql = "CALL public.insert_user_record(?,?,?,?)";
        jdbcTemplate.update(sql,userDto.getName(),
               passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail(),
                userDto.getUsername());

    }
    public UserDto get_user_credentials(String credentials){
        String sql = "SELECT * FROM public.get_user_credentials(?)";
        return jdbcOperations.queryForObject(sql,new Object[]{credentials}, new userRowMapper());
    }
}
