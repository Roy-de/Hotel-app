package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.ExceptionHandlers.Exception.DatabaseException;
import com.example.hotelapp.Mappers.AdminRowMapper;
import com.example.hotelapp.Mappers.HotelObjectMapper;
import com.example.hotelapp.Repository.AdminRepository;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private final HotelServiceLayer hotelServiceLayer;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryImpl(HotelServiceLayer hotelServiceLayer, JdbcTemplate jdbcTemplate) {
        this.hotelServiceLayer = hotelServiceLayer;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_account(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto, List<HotelImagesDto> hotelImagesDtoList) {
        String sql = "SELECT public.create_admin_and_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Object[] params = {
                adminDto.getUsername(),
                adminDto.getEmail(),
                passwordEncoder.encode(adminDto.getPassword()),
                adminDetailsDto.getFirst_name(),
                adminDetailsDto.getLast_name(),
                adminDetailsDto.getPhone_no(),
                hotelDto.getName(),
                hotelDto.getLocation().toLowerCase(),
                hotelDto.getPlace().toLowerCase(),
                hotelDto.getDescription(),
                hotelDto.getPricing(),
                hotelDto.getNo_of_beds(),
                hotelDto.getRooms_available(),
                hotelDto.getLongitude(),
                hotelDto.getLatitude(),
                adminDetailsDto.getAlt_phone_no(),
                adminDetailsDto.getAlt_email()
        };
        List<Integer> result = jdbcTemplate.query(sql, (rs, rowNum) -> rs.getInt(1), params);
        int hotel_id = result.isEmpty() ? 0 : result.get(0);

        if (hotel_id != 0) {
            hotelServiceLayer.insert_image(hotel_id, hotelImagesDtoList);
        }
    }


    @Override
    public AdminDto get_Admin_credentials(String credential) {
        String sql = "SELECT * FROM public.get_admin_credentials(?)";
        return jdbcTemplate.queryForObject(sql,new AdminRowMapper(),credential);
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
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(sql, Boolean.class,credentials));
    }

    @Override
    public List<HotelObject> get_all_hotels(int admin_id) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        String hotelSql = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(NULL,?,NULL)";
        return hotelObjectMapper.get_hotel_details_by_id(hotelSql, admin_id);
    }

    @Override
    public String delete_hotel(int id) {
        String sql = "DELETE FROM public.hotel WHERE id = ?";
        try{
            jdbcTemplate.update(sql,id);
            return "Successfully deleted hotel";
        }catch (DatabaseException e){
            return "Error: "+e;
        }
    }

}
