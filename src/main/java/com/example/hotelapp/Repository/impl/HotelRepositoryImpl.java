package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Mappers.HotelImageMapper;
import com.example.hotelapp.Mappers.HotelRowMapper;
import com.example.hotelapp.Repository.HotelRepository;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
        /*String sql ="CALL public.create_hotel(:hotel_admin_id, :hotel_name, :hotel_location, :hotel_description," +
                " :hotel_pricing, :hotel_beds_no, :hotel_no_of_rooms, :hotel_longitude, :hotel_latitude" +
                ", :hotel_place, :h_views , :h_entertainment, :h_parking , :h_washing_machine," +
                " :h_swimming, :h_wifi, :h_bar, :h_breakfast, :h_fitness_centre, :h_restaurant" +
                ", :h_room_services)";
        Object[] params = {
                adminDto.getId(),
                hotelDto.getName(),
                hotelDto.getLocation(),
                hotelDto.getDescription(),
                hotelDto.getPricing(),
                hotelDto.getNo_of_beds(),
                hotelDto.getRooms_available(),
                hotelDto.getLongitude(),
                hotelDto.getLatitude(),
                hotelDto.getPlace(),
                hotelServicesDto.isViews(),
                hotelServicesDto.isEntertainment(),
                hotelServicesDto.isParking(),
                hotelServicesDto.isWashing_services(),
                hotelServicesDto.isSwimming_pool(),
                hotelServicesDto.isWifi(),
                hotelServicesDto.isBar(),
                hotelServicesDto.isBreakfast(),
                hotelServicesDto.isFitness_centre(),
                hotelServicesDto.isRestaurant(),
                hotelServicesDto.isRoom_services()
        };
        jdbcTemplate.update(sql,params)*/
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
    public HotelObject get_hotel(int id) {
        String hotelSql = "SELECT * FROM public.hotel WHERE admin_id = ?";
        List<HotelDto> hotels =  jdbcTemplate.query(hotelSql,new Object[]{id},new BeanPropertyRowMapper<>(HotelDto.class));
        return null;
    }

    @Override
    public void delete_hotel(int id) {

    }
}
