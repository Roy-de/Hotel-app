package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Mappers.HotelObjectMapper;
import com.example.hotelapp.Repository.HotelRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Repository
public class HotelRepositoryImpl implements HotelRepository {
    AdminDto adminDto;
    private final JdbcTemplate jdbcTemplate;

    public HotelRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create_hotel(HotelDto hotelDto,HotelServicesDto hotelServicesDto) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement("CALL public.create_hotel(" +
                    "?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, adminDto.getId());
            ps.setString(2, hotelDto.getName());
            ps.setString(3, hotelDto.getLocation());
            ps.setString(4, hotelDto.getDescription());
            ps.setDouble(5, hotelDto.getPricing());
            ps.setLong(6, hotelDto.getNo_of_beds());
            ps.setLong(7, hotelDto.getRooms_available());
            ps.setString(8, hotelDto.getPlace());
            ps.setDouble(9, hotelDto.getLongitude());
            ps.setDouble(10, hotelDto.getLatitude());
            ps.setBoolean(11, hotelServicesDto.isViews());
            ps.setBoolean(12, hotelServicesDto.isEntertainment());
            ps.setBoolean(13, hotelServicesDto.isParking());
            ps.setBoolean(14, hotelServicesDto.isWashing_services());
            ps.setBoolean(15, hotelServicesDto.isSwimming_pool());
            ps.setBoolean(16, hotelServicesDto.isWifi());
            ps.setBoolean(17, hotelServicesDto.isBar());
            ps.setBoolean(18, hotelServicesDto.isBreakfast());
            ps.setBoolean(19, hotelServicesDto.isFitness_centre());
            ps.setBoolean(20, hotelServicesDto.isRestaurant());
            ps.setBoolean(21, hotelServicesDto.isRoom_services());
            return ps;
        },keyHolder);
        return Objects.requireNonNull(keyHolder.getKey()).intValue();
    }

    @Override
    public void insert_images(List<byte[]> images, List<String> descriptions,int Hotel_id) {
        String sql = "INSERT INTO public.hotel_images(image,description,hotel_id) VALUES(?,?,?)";
        List<Object[]> batchArgs = new ArrayList<>();
        for(int i = 0; i < images.size(); i++){
            byte[] image = images.get(i);
            String description = descriptions.get(i);
            if (image != null) {
                Object[] args = {image, description, Hotel_id};
                batchArgs.add(args);
            }
        }
        jdbcTemplate.batchUpdate(sql,batchArgs);
    }

    @Override
    public void edit_hotel_services(HotelServicesDto hotelServicesDto,int hotel_id) {
        String sql = "UPDATE public.hotel_services SET views = ?,entertainment = ?,parking = ?,washing_machine = ?,swimming = ?,wifi = ?,bar = ?,breakfast = ?,fitness_centre = ?,restaurant = ?,room_services = ? WHERE hotel_id = ?";
        jdbcTemplate.update(con -> {PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setBoolean(1,hotelServicesDto.isViews());
        ps.setBoolean(2,hotelServicesDto.isEntertainment());
            ps.setBoolean(3,hotelServicesDto.isParking());
            ps.setBoolean(4,hotelServicesDto.isWashing_services());
            ps.setBoolean(5,hotelServicesDto.isSwimming_pool());
            ps.setBoolean(6,hotelServicesDto.isWifi());
            ps.setBoolean(7,hotelServicesDto.isBar());
            ps.setBoolean(8,hotelServicesDto.isBreakfast());
            ps.setBoolean(9,hotelServicesDto.isFitness_centre());
            ps.setBoolean(10,hotelServicesDto.isRestaurant());
            ps.setBoolean(11,hotelServicesDto.isRoom_services());
            ps.setInt(12,hotel_id);
            return ps;
        } );
    }

    @Override
    public void edit_hotel_images(List<HotelImagesDto> hotelImagesDto,int hotel_id) {

    }

    @Override
    public void edit_hotel_details(HotelDto hotelDto,int id) {
        String sql =  "UPDATE public.hotel SET name = ?,location = ?,description = ?,pricing = ?,no_of_beds = ?,no_of_rooms = ?,longitude = ?,latitude = ?,place = ? WHERE id = ?";
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,hotelDto.getName());
            ps.setString(2,hotelDto.getLocation());
            ps.setString(3,hotelDto.getDescription());
            ps.setDouble(4,hotelDto.getPricing());
            ps.setInt(5,hotelDto.getNo_of_beds());
            ps.setInt(6,hotelDto.getRooms_available());
            ps.setDouble(7,hotelDto.getLongitude());
            ps.setDouble(8,hotelDto.getLatitude());
            ps.setString(9,hotelDto.getPlace());
            return ps;
        });
    }

    @Override
    public void delete_image(List<Integer> id) {

    }

    @Override
    public List<HotelObject> list_all_hotels(String location) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        String hotelSql = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(?,NULL,NULL)";
        List<HotelObject> hotels = hotelObjectMapper.get_hotel_details_by_location(hotelSql, location);
        if (!hotels.isEmpty()) {
            return hotels;
        } else {
            return null; // or throw an exception indicating no hotel found
        }
    }

    @Override
    public HotelObject get_hotel_by_id(int id) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        String hotelSql = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(NULL,NULL,?)";
        List<HotelObject> hotels = hotelObjectMapper.get_hotel_details_by_id(hotelSql, id);
        if (!hotels.isEmpty()) {
            return hotels.get(0);
        } else {
            return null; // or throw an exception indicating no hotel found
        }
    }
}
