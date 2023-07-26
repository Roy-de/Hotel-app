package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Booking.BookingDto;
import com.example.hotelapp.Mappers.BookingMapper;
import com.example.hotelapp.Repository.BookingRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
public class BookingRepositoryImpl implements BookingRepository {
    private static final String CREATE_BOOKING = "INSERT INTO public.booking( checkin, checkout, rooms, adults, children, hotel_id, user_name) VALUES (?,?,?,?,?,?,?)";
    private static final String LIST_BOOKING = "SELECT * FROM public.booking WHERE hotel_id = ?";
    private static final String DELETE_BOOKING = "DELETE FROM public.booking WHERE id = ?";
    private final JdbcTemplate jdbcTemplate;

    public BookingRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_booking(BookingDto bookingDto,int id) {
        Object[] params = {
                bookingDto.getCheckin(),
                bookingDto.getCheckout(),
                bookingDto.getRooms(),
                bookingDto.getAdults(),
                bookingDto.getChildren(),
                id,
                bookingDto.getUsername()
        };
        try{
            jdbcTemplate.update(CREATE_BOOKING,params);
        }catch (DataAccessException e){
            log.warn(e.getMessage());
        }

    }

    @Override
    public List<BookingDto> list_booking(int id) {
        try{
            return jdbcTemplate.query(LIST_BOOKING,new BookingMapper(),id);
        }catch (DataAccessException e){
            log.warn(e.getMessage());
            return null;
        }
    }

    @Override
    public void delete_booking(int id) {
        try{
            jdbcTemplate.update(DELETE_BOOKING,id);
        }catch (DataAccessException e){
            log.warn(e.getMessage());
        }
    }
}
