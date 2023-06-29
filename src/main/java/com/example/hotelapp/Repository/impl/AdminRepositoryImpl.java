package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.Repository.AdminRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryImpl(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_account(AdminDto adminDto,AdminDetailsDto adminDetailsDto, HotelDto hotelDto) {
       /* String sql = "INSERT INTO public.admin_acc(username,email,password) VALUES (?,?,?)";
        Object[] params = {
                adminDto.getUsername(),
                adminDto.getEmail(),
                passwordEncoder.encode(adminDto.getPassword())
        };
        jdbcTemplate.update(sql,params);*/
        String sql = "CALL public.create_admin_and_hotel(:admin_username, :admin_email, :admin_password , :admin_first_name, " +
                ":admin_last_name,:admin_phone_no,:hotel_name, :hotel_location, " +
                ":hotel_place, :hotel_description, :hotel_pricing, :hotel_no_of_beds, " +
                ":hotel_no_of_rooms, :hotel_longitude, :hotel_latitude, :alt_admin_phone_no, :alt_admin_email)";
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("admin_username",adminDto.getUsername());
        paramMap.put("admin_email",adminDto.getEmail());
        paramMap.put("admin_password",passwordEncoder.encode(adminDto.getPassword()));
        //--------------------------------------------------------
        paramMap.put("admin_first_name",adminDetailsDto.getFirst_name());
        paramMap.put("admin_last_name",adminDetailsDto.getLast_name());
        paramMap.put("admin_phone_no",adminDetailsDto.getPhone_no());
        paramMap.put("alt_admin_phone_no",adminDetailsDto.getAlt_phone_no());
        paramMap.put("alt_admin_email",adminDetailsDto.getAlt_email());
        //--------------------------------------------------------
        paramMap.put("hotel_name",hotelDto.getName());
        paramMap.put("hotel_location",hotelDto.getLocation().toLowerCase());
        paramMap.put("hotel_place",hotelDto.getPlace().toLowerCase());
        paramMap.put("hotel_description",hotelDto.getDescription());
        paramMap.put("hotel_pricing",hotelDto.getPricing());
        paramMap.put("hotel_no_of_beds",hotelDto.getNo_of_beds());
        paramMap.put("hotel_no_of_rooms",hotelDto.getRooms_available());
        paramMap.put("hotel_longitude",hotelDto.getLongitude());
        paramMap.put("hotel_latitude",hotelDto.getLatitude());

        namedParameterJdbcTemplate.update(sql,paramMap);
    }

    @Override
    public AdminDto get_Admin_credentials(AdminDto adminDto) {
        return null;
    }

/*
    @Override
    public AdminDto get_Admin_credentials(AdminDto adminDto) {
        String sql = "SELECT * FROM public.get_admin_credentials(?)";
        return jdbcTemplate.query(sql,)
    }
*/

    @Override
    public boolean search_for_username(String credentials) {
        String sql = "SELECT * FROM public.check_admin_credentials(?)";
        Object[] param = {credentials};
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, param, Boolean.class));
    }

}
