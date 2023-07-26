package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelImageMapper implements RowMapper<HotelImagesDto> {
    @Override
    public HotelImagesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelImagesDto hotelImagesDto = new HotelImagesDto();
        hotelImagesDto.setId(rs.getInt("id"));
        hotelImagesDto.setImageUrl(rs.getString("imageurl"));
        hotelImagesDto.setHotel_id(rs.getInt("hotel_id"));
        hotelImagesDto.setDescription(rs.getString("description"));
        hotelImagesDto.setPublic_id(rs.getString("public_id"));
        return hotelImagesDto;
    }
}
