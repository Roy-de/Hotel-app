package com.example.hotelapp.DTO.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {
    private int id;
    private String name;
    private String location;
    private int rating;
    private String description;
    private double pricing;
    private int no_of_beds;
    private int rooms_available;
    private double longitude;
    private double latitude;
    private String place;
}
