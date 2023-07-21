package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**<p>This part might confuse some of you, so I will explain step by step</p>
 * <p>This class is responsible for building a hotel. i.e. it will fetch data based on the username,
 * hotel id and location</p>
 * <p>It will get details from 3 tables, Hotel table(responsible for storing hotel details), Hotel services and hotel images</p>
 *
 *
 */
@Slf4j
public class HotelObjectMapper {

    private final JdbcTemplate jdbcTemplate;

    public HotelObjectMapper(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This is the first method that is used to get the data based on the location queried
     * @param sql   Takes in the sql query
     * @param parameter Takes in the location as a parameter
     * @return  returns a method that creates the hotel object
     */
    public List<HotelObject> get_hotel_details_by_location(String sql, String parameter) {
        List<HotelDto> hotelDtos = jdbcTemplate.query(sql, new HotelRowMapper(), parameter);
        return createHotelObjects(hotelDtos);
    }
    /**
     * This is the second method that is used to get the data based on the hotel id
     * @param sql   Takes in the sql query
     * @param parameter Takes in the hotel id as a parameter
     * @return  returns a method that creates the hotel object
     */
    public List<HotelObject> get_hotel_details_by_id(String sql, int parameter) {
        List<HotelDto> hotelDtos = jdbcTemplate.query(sql, new HotelRowMapper(), parameter);
        return createHotelObjects(hotelDtos);
    }
    /**
     * This is the third method that is used to get the data based on the username queried
     * <p>This one is more secure to avoid anyone with an account from querying hotels that are not his/hers</p>
     * @param sql   Takes in the sql query
     * @param parameter Takes in the username of the hotel owner as a parameter
     * @return  returns a method that creates the hotel object
     */
    public List<HotelObject> get_hotel_details_by_username(String sql, String parameter) {
        List<HotelDto> hotelDtos = jdbcTemplate.query(sql, new HotelRowMapper(), parameter);
        return createHotelObjects(hotelDtos);
    }

    /**
     * <p>How do we create our hotel object</p>
     * <p>1. We need to get the hotel details from the above methods</p>
     * <p>2. We will use the hotel details that we got to get other details such as hotel services and hotel images</p>
     * <p>3. We will build our hotel object using the data we got using setters </p>
     * @param hotelDtos List of hotel details
     * @return  Hotel object
     */
    private List<HotelObject> createHotelObjects(List<HotelDto> hotelDtos) {
        //Initialize array of the hotel object
        List<HotelObject> hotelObjects = new ArrayList<>();
        for (HotelDto hotelDto : hotelDtos) {
            //Iterate trough every element in the hotelDto list and set the values
            HotelObject hotelObject = new HotelObject();
            hotelObject.setHotelDto(hotelDto);

            int hotelId = hotelObject.getHotelDto().getId();
            List<HotelImagesDto> images = get_hotel_images(hotelId);
            if(!Objects.requireNonNull(images).isEmpty()){
                hotelObject.setHotelImagesDto(images);
            }else{
                hotelObject.setHotelImagesDto(null);
            }
            HotelServicesDto services = getHotelServicesByHotelId(hotelId);
            hotelObject.setHotelServicesDto(services);

            hotelObjects.add(hotelObject);
        }
        return hotelObjects;
    }

    /**
     * This method gets hotel images form database using the hotel id
     * @param hotelId   hotel id
     * @return  hotel images (list)
     */
    private List<HotelImagesDto> get_hotel_images(int hotelId) {
        String imagesQuery = "SELECT id, image, description, hotel_id " +
                "FROM public.hotel_images " +
                "WHERE hotel_id = ?";
        try{
            return jdbcTemplate.query(imagesQuery, new HotelImageMapper(), hotelId);
        }catch (EmptyResultDataAccessException e){
            log.info("Empty result: {}",e.getMessage());
            return null;
        }
    }

    /**
     * This method gets hotel services from database based on hotel id
     * @param hotelId   hotel id
     * @return  services dto
     */
    private HotelServicesDto getHotelServicesByHotelId(int hotelId) {
        String servicesQuery = "SELECT hotel_id, views, entertainment, parking, washing_machine, " +
                "swimming, wifi, bar, breakfast, fitness_centre, restaurant, room_services " +
                "FROM hotel_services " +
                "WHERE hotel_id = ?";
        try {
            return jdbcTemplate.queryForObject(servicesQuery, new HotelServiceMapper(), hotelId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}

