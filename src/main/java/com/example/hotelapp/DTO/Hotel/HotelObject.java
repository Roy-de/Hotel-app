package com.example.hotelapp.DTO.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelObject {
    //This class is for all the dto s of a hotel
    private HotelDto hotelDto;
    private HotelImagesDto hotelImagesDto;
    private HotelServicesDto hotelServicesDto;
}
