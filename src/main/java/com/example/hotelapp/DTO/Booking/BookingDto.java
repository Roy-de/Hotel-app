package com.example.hotelapp.DTO.Booking;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Time;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    @DateTimeFormat
    private Date checkin;
    @DateTimeFormat
    private Date checkout;
    private int rooms;
    private int adults;
    private int children;
    private Time order_time;
    private String username;
    private int hotel_id;
}
