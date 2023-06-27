package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelObject;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelObjectMapper implements RowMapper<HotelObject> {
    @Override
    public HotelObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        return null;
    }
}
