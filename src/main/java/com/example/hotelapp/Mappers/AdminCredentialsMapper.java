package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminCredentialsMapper implements RowMapper<AdminCredential> {
    @Override
    public AdminCredential mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdminCredential adminCredential =  new AdminCredential();
        adminCredential.setId(rs.getInt("id"));
        adminCredential.setAdmin_username(rs.getString("username"));
        adminCredential.setAdmin_email(rs.getString("email"));
        adminCredential.setAdmin_password(rs.getString("password"));
        return adminCredential;
    }
}
