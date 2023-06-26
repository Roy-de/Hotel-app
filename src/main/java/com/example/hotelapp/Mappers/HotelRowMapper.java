package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelRowMapper implements RowMapper<HotelDto> {
    @Override
    public HotelDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setName(rs.getString("name"));
        hotelDto.setLocation(rs.getString("location"));
        hotelDto.setDescription(rs.getString("description"));
        hotelDto.setPricing(rs.getDouble("pricing"));
        hotelDto.setNo_of_beds(rs.getInt("no_of_beds"));
        hotelDto.setRooms_available(rs.getInt("no_of_rooms"));
        hotelDto.setLongitude(rs.getDouble("longitude"));
        hotelDto.setLatitude(rs.getDouble("latitude"));
        hotelDto.setPlace(rs.getString("place"));
        //hotelDto.setRating(rs.getInt("rating"));
        return hotelDto;
    }
}
