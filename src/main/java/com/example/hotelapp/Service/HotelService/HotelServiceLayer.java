package com.example.hotelapp.Service.HotelService;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Repository.impl.HotelRepositoryImpl;
import com.example.hotelapp.Service.ImageService.ImageServiceLayer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class HotelServiceLayer {
    private final ImageServiceLayer imageServiceLayer;
    private final HotelRepositoryImpl hotelRepository;

    public HotelServiceLayer(ImageServiceLayer imageServiceLayer, HotelRepositoryImpl hotelRepository) {
        this.imageServiceLayer = imageServiceLayer;
        this.hotelRepository = hotelRepository;
    }

    public List<HotelObject> list_all_hotels(String location){
        return hotelRepository.list_all_hotels(location.toLowerCase());
    }
    //List hotel and services offered and images it has
    public HotelObject getHotel(int hotel_id){
        return hotelRepository.get_hotel_by_id(hotel_id);
    }
    public void insert_image(int Hotel_id,List<HotelImagesDto> hotelImagesDtoList){
        List<String> public_ids = new ArrayList<>();
        List<String> secure_urls = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        for(HotelImagesDto hotelImagesDto: hotelImagesDtoList){
            String description = hotelImagesDto.getDescription();
            String public_id = hotelImagesDto.getPublic_id();
            String secure_url = hotelImagesDto.getImageUrl();
            public_ids.add(public_id);
            secure_urls.add(secure_url);
            descriptions.add(description);
            log.info("Secure URL in the service layer: {}",secure_url);
            log.info("Public ID in the service layer: {}",public_id);
        }
        hotelRepository.insert_images(public_ids,secure_urls,descriptions,Hotel_id);
    }
    public void create_hotel(int admin_id,HotelDto hotelDto,HotelServicesDto hotelServicesDto,List<HotelImagesDto> hotelImagesDtoList){
        //Create the hotel
        log.info("Hotel Dto: {}",hotelDto);
        log.info("Hotel services: {}",hotelServicesDto);
        log.info("Hotel images: {}",hotelImagesDtoList);
        int id = hotelRepository.create_hotel(admin_id,hotelDto,hotelServicesDto);
        //associate hotel images to hotel
        insert_image(id,hotelImagesDtoList);
    }
    public void update_service(HotelServicesDto hotelServicesDto, int hotel_id){
        hotelRepository.edit_hotel_services(hotelServicesDto,hotel_id);
    }
    public void update_hotel_detail(HotelDto hotelDto,int hotel_id){
        hotelRepository.edit_hotel_details(hotelDto,hotel_id);
    }
    public String update_images(List<HotelImagesDto> hotelImagesDto,int id){
        try{
            hotelRepository.edit_hotel_images(hotelImagesDto,id);
            return "Success";
        }catch (DataAccessException e){
            return e.getMessage();
        }

    }
    public List<HotelImagesDto> images(@RequestParam("images") List<MultipartFile> images,
                                       @RequestParam(value = "description", required = false) List<String> descriptions) throws IOException {
        List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();

        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            HotelImagesDto imagesDto = imageServiceLayer.uploadImage(image);

            // If descriptions are provided, assign the corresponding description to the image
            if (descriptions != null && i < descriptions.size()) {
                String description = descriptions.get(i);
                imagesDto.setDescription(description);
            }

            hotelImagesDtoList.add(imagesDto);
        }

        return hotelImagesDtoList;
    }
    public void delete_image(List<Integer> ids){
        hotelRepository.delete_image(ids);
    }
}
