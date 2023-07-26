package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDetailsDto;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.Mappers.HotelObjectMapper;
import com.example.hotelapp.Repository.AdminRepository;
import com.example.hotelapp.Service.HotelService.HotelServiceLayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@Slf4j
public class AdminRepositoryImpl implements AdminRepository {
    /**
     * In this class, we will perform all admin repository functions
     */
    //All SQL QUERIES
    private static final String CREATE_ACCOUNT = "SELECT public.create_admin_and_hotel(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SEARCH_USERNAME = "SELECT * FROM public.check_admin_credentials(?)";
    //language=PostgreSQL
    private static final String GET_HOTELS = "SELECT * FROM public.get_hotels_by_location_or_admin_or_id(NULL,?,NULL)";
    private static final String DELETE_HOTEL = "DELETE FROM public.hotel WHERE id = ?";
    private static final String CHECK_OWNER = "SELECT username FROM  public.admin_acc WHERE id IN(SELECT admin_id FROM public.hotel WHERE admin_id = ?)";
    private static final String EDIT_HOTEL = "UPDATE public.admin_details SET first_name=?,last_name=?,alt_phone_no = ?,phone_no=?,alt_email=? WHERE admin_id IN(SELECT id FROM public.admin_acc WHERE username = ?)";
    private static final String GET_ADMIN_ID = "SELECT id FROM public.admin_acc WHERE  username = ?";
    private final HotelServiceLayer hotelServiceLayer;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryImpl(HotelServiceLayer hotelServiceLayer, JdbcTemplate jdbcTemplate) {
        this.hotelServiceLayer = hotelServiceLayer;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * This method is used to create first time user account.
     * @param adminDto This gets admin credentials that will be used for authentication
     * @param adminDetailsDto This gets admin details to be used for contact
     * @param hotelDto  This gets the hotel details
     * @param hotelImagesDtoList  These are hotel images
     */
    @Override
    public void create_account(AdminDto adminDto, AdminDetailsDto adminDetailsDto, HotelDto hotelDto, List<HotelImagesDto> hotelImagesDtoList) {
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
        List<Integer> result = jdbcTemplate.query(CREATE_ACCOUNT, (rs, rowNum) -> rs.getInt(1), params);
        int hotel_id = result.isEmpty() ? 0 : result.get(0);

        if (hotel_id != 0) {
            hotelServiceLayer.insert_image(hotel_id, hotelImagesDtoList);
        }
    }

    /**Searches for a username in the database
     *
     * @param credentials Takes in username or email as an input
     * @return boolean true if username or email is found
     */
    @Override
    public boolean search_for_username(String credentials) {
        return Boolean.TRUE.equals(jdbcTemplate.queryForObject(SEARCH_USERNAME, Boolean.class,credentials));
    }

    /**Gets list of hotels for a specific admin ( i.e. Person who hosted the hotel)
     *
     * @param username  username of the hotel owner
     * @return Hotel object from the hotel object builder class
     */
    @Override
    public List<HotelObject> get_all_hotels(String username) {
        HotelObjectMapper hotelObjectMapper = new HotelObjectMapper(jdbcTemplate);
        return hotelObjectMapper.get_hotel_details_by_username(GET_HOTELS, username);
    }

    /**
     * Delete hotel based on the hotel id
     * @param id hotel id
     * @return response that is if it is successful or not
     */
    @Override
    public String delete_hotel(int id) {
        try{
            jdbcTemplate.update(DELETE_HOTEL,id);
            return "Successfully deleted hotel";
        }catch (DataAccessException e){
            return "Error: "+e.getMessage();
        }
    }
    /**Check if the person who is deleting hotel is the hotel owner*/
    public String check_owner(int id){
        return jdbcTemplate.queryForObject(CHECK_OWNER,String.class,id);
    }

    /** Edit admin details
     *
     * @param adminDetailsDto   Takes in the new admin details
     * @param username  Finds admin using the username
     * @return  returns the response
     */
    @Override
    public String edit_Details(AdminDetailsDto adminDetailsDto,String username) {
        try{
            jdbcTemplate.update(EDIT_HOTEL, adminDetailsDto.getFirst_name(), adminDetailsDto.getLast_name(), adminDetailsDto.getAlt_phone_no(), adminDetailsDto.getPhone_no(), adminDetailsDto.getAlt_email(), username);
            return "Success";
        }catch (DataAccessException e){

            return ("Error:" +e.getMessage());
        }
    }

    /**
     * This method is used to get admin id to be used in creation of a new hotel
     * @param username  username is used to get the id
     * @return  integer(id)
     */
    @Override
    public Integer get_admin_id(String username) {
        return jdbcTemplate.queryForObject(GET_ADMIN_ID, Integer.class, username);
    }

}
