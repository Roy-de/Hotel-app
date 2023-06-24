package com.example.hotelapp.DTO.Hotel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HotelServicesDto {
    private String name;
    private boolean views;
    private boolean television;
    private boolean parking;
    private boolean washing_services;
    private boolean swimming_pool;
    private boolean wifi;
    private boolean bar;
    private boolean breakfast;
    private boolean fitness_centre;
    private boolean restaurant;
    private boolean personel_services;
}
