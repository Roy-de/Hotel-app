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
        hotelServicesDto.setEntertainment(rs.getBoolean("entertainment"));
        hotelServicesDto.setParking(rs.getBoolean("parking"));
        hotelServicesDto.setWashing_services(rs.getBoolean("washing_machine"));
        hotelServicesDto.setSwimming_pool(rs.getBoolean("swimming"));
        hotelServicesDto.setWifi(rs.getBoolean("wifi"));
        hotelServicesDto.setBar(rs.getBoolean("bar"));
        hotelServicesDto.setBreakfast(rs.getBoolean("breakfast"));
        hotelServicesDto.setFitness_centre(rs.getBoolean("fitness_centre"));
        hotelServicesDto.setRestaurant(rs.getBoolean("restaurant"));
        hotelServicesDto.setRoom_services(rs.getBoolean("room_service"));
        return hotelServicesDto;
    }
}
