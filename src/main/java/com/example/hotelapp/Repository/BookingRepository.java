package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.Booking.BookingDto;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository {
    //Create new booking
    void create_booking(BookingDto bookingDto,int id);
    //See booking
    List<BookingDto> list_booking(int id);
    //Delete Booking
    void delete_booking(int id);
}
