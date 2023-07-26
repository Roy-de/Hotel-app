package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Mappers.HotelObjectMapper;
import com.example.hotelapp.Repository.HotelRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
@Slf4j
public class HotelRepositoryImpl implements HotelRepository {
    private static final String CREATE_HOTEL = "SELECT public.create_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String INSERT_IMAGES = "INSERT INTO public.hotel_images(public_id,imageurl,description,hotel_id) VALUES(?,?,?,?)";
    private static final String EDIT_SERVICES = "UPDATE public.hotel_services SET views = ?,entertainment = ?,parking = ?,washing_machine = ?,swimming = ?,wifi = ?,bar = ?,breakfast = ?,fitness_centre = ?,restaurant = ?,room_services = ? WHERE hotel_id = ?";
    private static final String EDIT_IMAGES = "UPDATE hotel_images SET imageurl = ?,public_id = ?,description  =? WHERE hotel_id = ?";
    private static final String EDIT_DETAILS = "UPDATE public.hotel SET name = ?,location = ?,description = ?,pricing = ?,no_of_beds = ?,no_of_rooms = ?,longitude = ?,latitude = ?,place = ? WHERE id = ?";
    private static final String DELETE_IMAGE = "DELETE FROM public.hotel_images WHERE id = ?";
    private static final String LIST_HOTELS = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(?,NULL,NULL)";
    private static final String GET_HOTEL_ID = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(NULL,NULL,?)";
    private final JdbcTemplate jdbcTemplate;

    public HotelRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int create_hotel(int admin_id,HotelDto hotelDto,HotelServicesDto hotelServicesDto) {

        Object[] params = {
                admin_id,
                hotelDto.getName(),
                hotelDto.getLocation(),
                hotelDto.getDescription(),
                hotelDto.getPricing(),
                hotelDto.getNo_of_beds(),
                hotelDto.getRooms_available(),
                hotelDto.getPlace(),
                hotelDto.getLongitude(),
                hotelDto.getLatitude(),
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
        Integer hotelId = jdbcTemplate.queryForObject(CREATE_HOTEL, Integer.class, params);
        if (hotelId != null) {
            return hotelId;
        } else {
            log.error("Failed to create hotel. Hotel ID is null.");
            // Handle the error, throw an exception, or return a default value (e.g., -1) based on your application logic
            throw new RuntimeException("Failed to create hotel. Hotel ID is null.");
        }

    }

    @Override
    public void insert_images(List<String> public_ids, List<String> secure_urls, List<String> descriptions, int Hotel_id) {
        List<Object[]> batchArgs = new ArrayList<>();
        for (int i = 0; i < public_ids.size(); i++) {
            String publicId = public_ids.get(i);
            String secureUrl = secure_urls.get(i);
            String description = descriptions.get(i);
            if (publicId != null && secureUrl != null) {
                Object[] args = {publicId, secureUrl, description, Hotel_id};
                batchArgs.add(args);
            }
        }
        jdbcTemplate.batchUpdate(INSERT_IMAGES, batchArgs);
    }

    @Override
    public void edit_hotel_services(HotelServicesDto hotelServicesDto,int hotel_id) {
        jdbcTemplate.update(con -> {PreparedStatement ps = con.prepareStatement(EDIT_SERVICES, Statement.RETURN_GENERATED_KEYS);
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
    @SuppressWarnings("Not annotated parameter overrides @NonNullApi parameter")
    public void edit_hotel_images(List<HotelImagesDto> hotelImagesDto,int hotel_id) {
        jdbcTemplate.batchUpdate(EDIT_IMAGES, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                String public_id = hotelImagesDto.get(i).getPublic_id();
                String description = hotelImagesDto.get(i).getDescription();
                String imageurl = hotelImagesDto.get(i).getImageUrl();
                ps.setString(1,imageurl);
                ps.setString(2,public_id);
                ps.setString(3,description);
                ps.setInt(4,hotel_id);
            }

            @Override
            public int getBatchSize() {
                return hotelImagesDto.size();
            }
        });
    }

    @Override
    public void edit_hotel_details(HotelDto hotelDto,int id) {
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(EDIT_DETAILS,Statement.RETURN_GENERATED_KEYS);
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
    public void delete_image(List<Integer> ids) {
        for(int image_id: ids){
            jdbcTemplate.update(DELETE_IMAGE,image_id);
        }
    }

    @Override
    public List<HotelObject> list_all_hotels(String location) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        List<HotelObject> hotels = hotelObjectMapper.get_hotel_details_by_location(LIST_HOTELS, location);
        log.info("Hotel: {}",hotels);
        if (!hotels.isEmpty()) {
            log.info("Returning hotel");
            return hotels;
        } else {
            log.info("Returning null");
            return null; // or throw an exception indicating no hotel found
        }
    }

    @Override
    public HotelObject get_hotel_by_id(int id) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        List<HotelObject> hotels = hotelObjectMapper.get_hotel_details_by_id(GET_HOTEL_ID, id);
        if (!hotels.isEmpty()) {
            return hotels.get(0);
        } else {
            return null;
        }
    }
}
