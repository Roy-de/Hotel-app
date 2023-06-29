package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Mappers.HotelImageMapper;
import com.example.hotelapp.Mappers.HotelRowMapper;
import com.example.hotelapp.Repository.HotelRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class HotelRepositoryImpl implements HotelRepository {
    private final JdbcTemplate jdbcTemplate;

    public HotelRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_hotel(HotelDto hotelDto) {

    }

    @Override
    public void insert_images(List<byte[]> images, List<String> descriptions,int Hotel_id) {
        String sql = "INSERT INTO public.hotel_images(image,description,hotel_id) VALUES(?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for(int i = 0; i < images.size(); i++){
            byte[] image = images.get(i);
            String description = descriptions.get(i);
            Object[] args = {image,description,Hotel_id};
            batchArgs.add(args);
        }
        jdbcTemplate.batchUpdate(sql,batchArgs);
    }

    @Override
    public void edit_hotel_services(HotelServicesDto hotelServicesDto) {

    }

    @Override
    public void edit_hotel_images(HotelImagesDto hotelImagesDto) {

    }

    @Override
    public void edit_hotel_details(HotelDto hotelDto) {

    }

    @Override
    public List<HotelDto> list_all_hotels(String location) {
        String sql = "SELECT * FROM public.get_hotels_by_location(?)";
        Object[] param =  {location};
        return jdbcTemplate.query(sql,param,new HotelRowMapper());
    }

    @Override
    public HotelServicesDto list_hotel_services(HotelServicesDto hotelServicesDto) {
        return null;
    }

    @Override
    public List<HotelImagesDto> get_hotel_images(int id) {
        String sql = "SELECT * FROM public.hotel_images where hotel_id = (?)";
        Object[] param = {id};
        return jdbcTemplate.query(sql,param, new HotelImageMapper());
    }

    @Override
    public void delete_hotel(HotelDto hotelDto) {

    }
}
