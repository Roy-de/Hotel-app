package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Admin.AdminDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDtoRowMapper implements RowMapper<AdminDto> {
    @Override
    public AdminDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdminDto adminDto = new AdminDto();
        adminDto.setUsername(rs.getString("username"));
        adminDto.setEmail(rs.getString("email"));
        adminDto.setPassword(rs.getString("password"));
        return adminDto;
    }
}
