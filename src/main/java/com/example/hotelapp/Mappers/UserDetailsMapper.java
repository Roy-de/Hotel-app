package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.User.UserDetailsDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDetailsMapper implements RowMapper<UserDetailsDto> {
    @Override
    public UserDetailsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserDetailsDto userDetailsDto = new UserDetailsDto();
        userDetailsDto.setFirst_name(rs.getString("first_name"));
        userDetailsDto.setLast_name(rs.getString("last_name"));
        userDetailsDto.setEmail(rs.getString("email"));
        userDetailsDto.setPhone_no(rs.getString("phone_no"));
        userDetailsDto.setUsername(rs.getString("username"));
        return userDetailsDto;
    }
}
