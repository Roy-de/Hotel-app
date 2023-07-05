package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelImageMapper implements RowMapper<HotelImagesDto> {
    @Override
    public HotelImagesDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelImagesDto hotelImagesDto = new HotelImagesDto();
        hotelImagesDto.setId(rs.getInt(1));
        hotelImagesDto.setImage(rs.getBytes(2));
        hotelImagesDto.setHotel_id(rs.getInt(4));
        hotelImagesDto.setDescription(rs.getString(3));
        return hotelImagesDto;
    }
}
