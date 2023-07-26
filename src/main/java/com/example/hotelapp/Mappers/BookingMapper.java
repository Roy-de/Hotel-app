package com.example.hotelapp.Mappers;

import com.example.hotelapp.DTO.Booking.BookingDto;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingMapper implements RowMapper<BookingDto> {
    @Override
    public BookingDto mapRow(ResultSet rs, int rowNum) throws SQLException {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setCheckin(rs.getDate("checkin"));
        bookingDto.setCheckout(rs.getDate("checkout"));
        bookingDto.setRooms(rs.getInt("rooms"));
        bookingDto.setAdults(rs.getInt("adults"));
        bookingDto.setChildren(rs.getInt("children"));
        bookingDto.setOrder_time(rs.getTime("order_time"));
        bookingDto.setUsername("user_name");
        return bookingDto;
    }
}
