package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelRowMapper implements RowMapper<HotelDto> {
    @Override
    public HotelDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(rs.getInt(1));
        hotelDto.setName(rs.getString(2));
        hotelDto.setLocation(rs.getString(3));
        hotelDto.setDescription(rs.getString(4));
        hotelDto.setPricing(rs.getDouble(5));
        hotelDto.setNo_of_beds(rs.getInt(6));
        hotelDto.setRooms_available(rs.getInt(7));
        hotelDto.setLongitude(rs.getDouble(8));
        hotelDto.setLatitude(rs.getDouble(9));
        hotelDto.setPlace(rs.getString(10));
        hotelDto.setRating(rs.getInt(11));
        return hotelDto;
    }
}
