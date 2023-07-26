package com.example.hotelapp.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CloudinaryConfig {
    private static final String CLOUD_NAME = "dxwnia3aa";
    private static final String API_KEY = "281987913725988";
    private static final String API_SECRET = "W7Lk0GMHK_kaHOVwJUjAjHHhzdg";
    @Bean
    public Cloudinary cloudinary(){
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name",CLOUD_NAME
                ,"api_key",API_KEY
                ,"api_secret",API_SECRET
                ,"secure",true));
    }
}
