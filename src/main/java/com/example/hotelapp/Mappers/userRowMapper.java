package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.User.UserCredentials;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class userRowMapper implements RowMapper<UserCredentials> {
    @Override
    public UserCredentials mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserCredentials user = new UserCredentials();
        user.setUser_username(rs.getString("user_username"));
        user.setUser_email(rs.getString("user_email"));
        user.setUser_password(rs.getString("user_password"));
        return user;
    }
}
