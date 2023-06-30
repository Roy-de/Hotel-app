package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelServiceMapper implements RowMapper<HotelServicesDto> {
    @Override
    public HotelServicesDto mapRow(ResultSet rs, int rowNum) throws SQLException {

        HotelServicesDto hotelServicesDto = new HotelServicesDto();
        hotelServicesDto.setViews(rs.getBoolean("view"));
        HotelObjectMapper.Services(rs, hotelServicesDto);
        hotelServicesDto.setRoom_services(rs.getBoolean("room_service"));
        return hotelServicesDto;
    }
}
