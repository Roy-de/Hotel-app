package com.example.hotelapp.Service.BookingService;

import com.example.hotelapp.DTO.Booking.BookingDto;
import com.example.hotelapp.Repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingServiceLayer {
    private final BookingRepository bookingRepository;

    public BookingServiceLayer(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public void createBooking(BookingDto bookingDto,int id){
        bookingRepository.create_booking(bookingDto,id);
    }
    public List<BookingDto> listBooking(int id){
        return bookingRepository.list_booking(id);
    }
    public void deleteBooking(int id){
        bookingRepository.delete_booking(id);
    }
}
