package com.example.hotelapp.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.text.DateFormat;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    @DateTimeFormat
    private DateFormat checkin;
    @DateTimeFormat
    private DateFormat checkout;
    private int rooms;
    private int adults;
    private int children;
    private DateFormat order_time;
    private int hotel_id;
}
