package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelImageMapper implements RowMapper<HotelImagesDto> {
    @Override
    public HotelImagesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelImagesDto hotelImagesDto = new HotelImagesDto();
        hotelImagesDto.setImage(rs.getByte("image"));
        hotelImagesDto.setHotel_id(rs.getInt("hotel_id"));
        hotelImagesDto.setDescription(rs.getString("description"));
        return hotelImagesDto;
    }
}
