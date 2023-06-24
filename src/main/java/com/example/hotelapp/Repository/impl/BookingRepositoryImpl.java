package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Booking.BookingDto;
import com.example.hotelapp.Repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    @Override
    public void create_booking(BookingDto bookingDto) {

    }

    @Override
    public List<BookingDto> list_booking(BookingDto bookingDto) {
        return null;
    }

    @Override
    public void delete_booking(BookingDto bookingDto) {

    }
}
