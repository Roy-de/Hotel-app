package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Booking.BookingDto;
import com.example.hotelapp.DTO.ChangedPassword;
import com.example.hotelapp.DTO.Comments.CommentDto;
import com.example.hotelapp.DTO.User.UserDetailsDto;
import com.example.hotelapp.DTO.User.UserDto;
import com.example.hotelapp.DTO.User.UserUpdatedDto;
import com.example.hotelapp.Mappers.UserDetailsMapper;
import com.example.hotelapp.Repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class UserRepositoryImpl implements UserRepository{
    private static final String CREATE_ACCOUNT = "CALL public.insert_user_record(?, ?, ?, ?)";
    private static final String CREATE_COMMENT = "INSERT INTO public.comments VALUES (username = ?,rating = ?,comment = ?,hotel_id = ?)";
    private static final String CREATE_BOOKING = "INSERT INTO public.booking VALUES (checkin = ?,checkout = ?,rooms = ?,adults = ?,children = ?,hotel_id = ?,user_name = ?)";
    private static final String GET_USER = "SELECT * FROM public.user_details WHERE user_details.user_account_id IN (SELECT id FROM public.user_account WHERE user_account.username = ? OR user_account.email = ?)";
    private static final String UPDATE_USER = "UPDATE public.user_details SET first_name = ?,last_name = ?,phone_no = ? where username = ? or email = ?";
    private static final String DELETE_USER = "DELETE FROM public.user_account WHERE email = ?";
    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder =  new BCryptPasswordEncoder();

    public UserRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_user_account(UserDto userDto) {
        log.info("Inside create method");
        jdbcTemplate.update(CREATE_ACCOUNT,userDto.getName(),
                passwordEncoder.encode(userDto.getPassword()),
                userDto.getEmail(),
                userDto.getUsername());
    }

    @Override
    public void create_comment(CommentDto commentDto, String username) {
        try{
            jdbcTemplate.update(CREATE_COMMENT,username,commentDto.getRating(),commentDto.getComment(),commentDto.getHotel_id());
        }catch (DataAccessException e){
            log.info("Error: {}",e.getMessage());
        }
    }

    @Override
    public void create_booking(BookingDto bookingDto, String username) {
        try{
            jdbcTemplate.update(CREATE_BOOKING,bookingDto.getCheckin(),bookingDto.getCheckout(),bookingDto.getRooms(),bookingDto.getAdults(),bookingDto.getChildren(),bookingDto.getHotel_id(),username);
        }catch (Exception e){
            log.info("Error: {}",e.getMessage());
        }
    }

    @Override
    public UserDetailsDto get_user(String credentials) {
        return jdbcTemplate.queryForObject(GET_USER,new UserDetailsMapper(),credentials,credentials);
    }

    @Override
    public void update_user(UserUpdatedDto userUpdatedDto, String credentials) {
        Object[] params = {
                userUpdatedDto.getFirst_name(),
                userUpdatedDto.getLast_name(),
                userUpdatedDto.getPhone_no(),
                credentials,
                credentials
        };

        jdbcTemplate.update(UPDATE_USER, params);
    }

    @Override
    public ChangedPassword change_password(UserDetailsDto userDetailsDto) {
        return null;
    }

    @Override
    public void delete_user(UserDto userDto) {
        jdbcTemplate.update(DELETE_USER,userDto.getUsername());
    }
}
