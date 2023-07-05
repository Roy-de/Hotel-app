package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

public class HotelObjectMapper {
    private final JdbcTemplate jdbcTemplate;

    public HotelObjectMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<HotelObject> get_hotel_details_by_location(String sql, String parameter) {
        List<HotelDto> hotelDtos = jdbcTemplate.query(sql, new HotelRowMapper(), parameter);
        return createHotelObjects(hotelDtos);
    }

    public List<HotelObject> get_hotel_details_by_id(String sql, int parameter) {
        List<HotelDto> hotelDtos = jdbcTemplate.query(sql, new HotelRowMapper(), parameter);
        return createHotelObjects(hotelDtos);
    }

    private List<HotelObject> createHotelObjects(List<HotelDto> hotelDtos) {
        List<HotelObject> hotelObjects = new ArrayList<>();
        for (HotelDto hotelDto : hotelDtos) {
            HotelObject hotelObject = new HotelObject();
            hotelObject.setHotelDto(hotelDto);

            int hotelId = hotelObject.getHotelDto().getId();
            List<HotelImagesDto> images = get_hotel_images(hotelId);
            hotelObject.setHotelImagesDto(images);

            HotelServicesDto services = getHotelServicesByHotelId(hotelId);
            hotelObject.setHotelServicesDto(services);

            if (services == null || images == null) {
                return null; // Return null if hotel services or images are missing
            }

            hotelObjects.add(hotelObject);
        }
        return hotelObjects;
    }

    private List<HotelImagesDto> get_hotel_images(int hotelId) {
        String imagesQuery = "SELECT id, image, description, hotel_id " +
                "FROM public.hotel_images " +
                "WHERE hotel_id = ?";
        return jdbcTemplate.query(imagesQuery, new HotelImageMapper(), hotelId);
    }

    private HotelServicesDto getHotelServicesByHotelId(int hotelId) {
        String servicesQuery = "SELECT hotel_id, views, entertainment, parking, washing_machine, " +
                "swimming, wifi, bar, breakfast, fitness_centre, restaurant, room_services " +
                "FROM hotel_services " +
                "WHERE hotel_id = ?";
        try {
            return jdbcTemplate.queryForObject(servicesQuery, new HotelServiceMapper(), hotelId);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if no hotel services found
        }
    }
}

