package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Repository.impl.AdminRepositoryImpl;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HotelObjectMapper implements RowMapper<HotelObject> {
    @Override
    public HotelObject mapRow(ResultSet rs, int rowNum) throws SQLException {
        HotelObject hotelObject = new HotelObject();
        /*--------------hotel dto mapper-------------*/
        HotelDto hotelDto = new HotelDto();
        hotelDto.setId(rs.getInt("id"));

        /*------------hotel images mapper----------*/
        List<HotelImagesDto> hotelImagesDtos = new ArrayList<>();
        do{
            HotelImagesDto hotelImagesDto = new HotelImagesDto();
            hotelImagesDto.setId(rs.getInt("id"));
            hotelImagesDto.setImage(rs.getBytes("image"));
            hotelImagesDto.setDescription(rs.getString("description"));
            hotelImagesDto.setHotel_id(rs.getInt("hotel_id"));
            hotelImagesDtos.add(hotelImagesDto);
        }while(rs.next());
        hotelObject.setHotelImagesDto(hotelImagesDtos);
        /*------------hotel services mapper---------------*/
        HotelServicesDto hotelServicesDto = new HotelServicesDto();
        hotelServicesDto.setViews(rs.getBoolean("views"));
        Services(rs, hotelServicesDto);
        hotelServicesDto.setRoom_services(rs.getBoolean("room_services"));
        hotelServicesDto.setHotel_id(rs.getInt("hotel_id"));

        return hotelObject;
    }

    static void Services(ResultSet rs, HotelServicesDto hotelServicesDto) throws SQLException {
        AdminRepositoryImpl.services(rs, hotelServicesDto);
    }
}
