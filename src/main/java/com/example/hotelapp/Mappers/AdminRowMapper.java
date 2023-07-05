package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Admin.AdminDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminRowMapper implements RowMapper<AdminDto> {
    @Override
    public AdminDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        AdminDto adminDto = new AdminDto();
        adminDto.setUsername(rs.getString(1));
        adminDto.setEmail(rs.getString(2));
        adminDto.setPassword(rs.getString(3));
        return adminDto;
    }
}
