package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Repository.AdminRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
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

    @Override
    public List<HotelObject> get_all_hotels(int admin_id) {
        String hotelQuery = "SELECT id, name, location, description, pricing, no_of_beds, " +
                "no_of_rooms, longitude, latitude, place " +
                "FROM public.hotel " +
                "WHERE admin_id = ?";

        List<HotelObject> hotelObjects = jdbcTemplate.query(hotelQuery, new Object[]{admin_id},
                (rs, rowNum) -> {
                    HotelObject hotelObject = new HotelObject();
                    HotelDto hotelDto = new HotelDto();
                    hotelDto.setId(rs.getInt("id"));
                    hotelDto.setName(rs.getString("name"));
                    hotelDto.setLocation(rs.getString("location"));
                    hotelDto.setDescription(rs.getString("description"));
                    hotelDto.setPricing(rs.getDouble("pricing"));
                    hotelDto.setNo_of_beds(rs.getInt("no_of_beds"));
                    hotelDto.setRooms_available(rs.getInt("no_of_rooms"));
                    hotelDto.setLongitude(rs.getDouble("longitude"));
                    hotelDto.setLatitude(rs.getDouble("latitude"));
                    hotelDto.setPlace(rs.getString("place"));
                    hotelObject.setHotelDto(hotelDto);
                    return hotelObject;
                });

        for (HotelObject hotelObject : hotelObjects) {
            int hotelId = hotelObject.getHotelDto().getId();


            List<HotelImagesDto> images = getHotelImagesByHotelId(hotelId);
            hotelObject.setHotelImagesDto(images);

            HotelServicesDto services = getHotelServicesByHotelId(hotelId);
            hotelObject.setHotelServicesDto(services);
        }

        return hotelObjects;
    }
   /* private HotelDto getHotel(int admin_id){
        String hotelQuery = "SELECT id, name, location, description, pricing, no_of_beds, " +
                "no_of_rooms, longitude, latitude, place " +
                "FROM public.hotel " +
                "WHERE admin_id = ?";
        return jdbcTemplate.query(hotelQuery,new Object[]{admin_id},(rs -> {
            HotelDto hotelDto = new HotelDto();
            hotelDto.setId(rs.getInt(rs.getInt("id")));
            hotelDetails(rs, hotelDto);
            return hotelDto;
        }));
    }*/

    private List<HotelImagesDto> getHotelImagesByHotelId(int hotelId) {
        String imagesQuery = "SELECT id, image, description, hotel_id " +
                "FROM hotel_images " +
                "WHERE hotel_id = ?";

        return jdbcTemplate.query(imagesQuery, new Object[]{hotelId},
                (rs, rowNum) -> {
                    HotelImagesDto hotelImagesDto = new HotelImagesDto();
                    hotelImagesDto.setId(rs.getInt("id"));
                    hotelImagesDto.setImage(rs.getBytes("image"));
                    hotelImagesDto.setDescription(rs.getString("description"));
                    hotelImagesDto.setHotel_id(rs.getInt("hotel_id"));
                    return hotelImagesDto;
                });
    }

    private HotelServicesDto getHotelServicesByHotelId(int hotelId) {
        String servicesQuery = "SELECT hotel_id, views, entertainment, parking, washing_machine, " +
                "swimming, wifi, bar, breakfast, fitness_centre, restaurant, room_services " +
                "FROM hotel_services " +
                "WHERE hotel_id = ?";

        return jdbcTemplate.queryForObject(servicesQuery, new Object[]{hotelId},
                (rs, rowNum) -> {
                    HotelServicesDto hotelServicesDto = new HotelServicesDto();
                    hotelServicesDto.setHotel_id(rs.getInt("hotel_id"));
                    hotelServicesDto.setViews(rs.getBoolean("views"));
                    services(rs, hotelServicesDto);
                    hotelServicesDto.setRoom_services(rs.getBoolean("room_services"));
                    return hotelServicesDto;
                });
    }

    public static void services(ResultSet rs, HotelServicesDto hotelServicesDto) throws SQLException {
        hotelServicesDto.setEntertainment(rs.getBoolean("entertainment"));
        hotelServicesDto.setParking(rs.getBoolean("parking"));
        hotelServicesDto.setWashing_services(rs.getBoolean("washing_machine"));
        hotelServicesDto.setSwimming_pool(rs.getBoolean("swimming"));
        hotelServicesDto.setWifi(rs.getBoolean("wifi"));
        hotelServicesDto.setBar(rs.getBoolean("bar"));
        hotelServicesDto.setBreakfast(rs.getBoolean("breakfast"));
        hotelServicesDto.setFitness_centre(rs.getBoolean("fitness_centre"));
        hotelServicesDto.setRestaurant(rs.getBoolean("restaurant"));
    }
}
