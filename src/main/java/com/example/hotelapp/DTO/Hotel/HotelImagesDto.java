package com.example.hotelapp.DTO.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelImagesDto {
    private int id;
    private byte[] image;
    private String description;
    private int hotel_id;

    public HotelImagesDto(byte[] image, String description) {
        this.image = image;
        this.description = description;
    }
}
