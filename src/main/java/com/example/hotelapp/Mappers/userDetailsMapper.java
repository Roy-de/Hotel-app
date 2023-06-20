package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.UserDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class userDetailsMapper implements RowMapper<UserDto> {
    @Override
    public UserDto mapRow(ResultSet rs, int rowNum) throws SQLException {
            UserDto user = new UserDto();
            user.setName(rs.getString("user_name"));
            user.setUsername(rs.getString("user_username"));
            user.setEmail(rs.getString("user_email"));
            return user;
    }
}
