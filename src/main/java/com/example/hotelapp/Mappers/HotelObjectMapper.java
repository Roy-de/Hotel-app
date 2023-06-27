package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class HotelObjectMapper implements RowMapper<HotelObject> {
    HotelDto hotelDto;
    HotelImagesDto hotelImagesDto;
    HotelServicesDto hotelServicesDto;
    @Override
    public HotelObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelObject hotelObject = new HotelObject();
        /*-----hotel dto mapper --------*/
        //hotelObject.setHotelDto();
        return hotelObject;
    }
}
