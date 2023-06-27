package com.example.hotelapp.DTO.Hotel;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class HotelObject {
    //This class is for all the dto s of a hotel
    private HotelDto hotelDto;
    private List<HotelImagesDto> hotelImagesDto;
    private HotelServicesDto hotelServicesDto;
    public HotelObject(HotelDto hotelDto, List<HotelImagesDto> hotelImagesDto, HotelServicesDto hotelServicesDto) {
        this.hotelDto = hotelDto;
        this.hotelImagesDto = hotelImagesDto;
        this.hotelServicesDto = hotelServicesDto;
    }
}
