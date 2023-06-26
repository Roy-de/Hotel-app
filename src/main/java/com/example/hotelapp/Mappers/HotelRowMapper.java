package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelRowMapper implements RowMapper<HotelDto> {
    @Override
    public HotelDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(rs.getInt("primary_hotel_id"));
        hotelDto.setName(rs.getString("hotel_name"));
        hotelDto.setLocation(rs.getString("hotel_location"));
        hotelDto.setDescription(rs.getString("hotel_description"));
        hotelDto.setPricing(rs.getDouble("hotel_pricing"));
        hotelDto.setNo_of_beds(rs.getInt("hotel_no_of_beds"));
        hotelDto.setRooms_available(rs.getInt("hotel_no_of_rooms"));
        hotelDto.setLongitude(rs.getDouble("hotel_longitude"));
        hotelDto.setLatitude(rs.getDouble("hotel_latitude"));
        hotelDto.setPlace(rs.getString("hotel_place"));
        hotelDto.setRating(rs.getInt("hotel_rating"));
        return hotelDto;
    }
}
