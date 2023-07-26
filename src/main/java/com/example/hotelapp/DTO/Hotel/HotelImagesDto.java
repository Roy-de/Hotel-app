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
    private String public_id;
    private String imageUrl;
    private String description;
    private int hotel_id;

    public HotelImagesDto(String public_id, String imageUrl, String description) {
        this.public_id = public_id;
        this.imageUrl = imageUrl;
        this.description = description;
    }
}
