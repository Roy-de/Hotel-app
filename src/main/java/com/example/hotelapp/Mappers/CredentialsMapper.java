package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.CredentialsDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CredentialsMapper implements RowMapper<CredentialsDto> {
    @Override
    public CredentialsDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        CredentialsDto credentials = new CredentialsDto();
        credentials.setId(rs.getInt("id"));
        credentials.setUsername(rs.getString("username"));
        credentials.setPassword(rs.getString("password"));
        return credentials;
    }
}
