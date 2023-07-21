package com.example.hotelapp.Service.HotelService;

import com.example.hotelapp.DTO.Hotel.HotelDto;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import com.example.hotelapp.DTO.Hotel.HotelObject;
import com.example.hotelapp.DTO.Hotel.HotelServicesDto;
import com.example.hotelapp.Repository.impl.HotelRepositoryImpl;
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
    private final HotelRepositoryImpl hotelRepository;

    public HotelServiceLayer(HotelRepositoryImpl hotelRepository) {
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
        List<byte[]> images = new ArrayList<>();
        List<String> descriptions = new ArrayList<>();

        for(HotelImagesDto hotelImagesDto: hotelImagesDtoList){
            byte[] imageFile = hotelImagesDto.getImage();
            String description = hotelImagesDto.getDescription();
            images.add(imageFile);
            descriptions.add(description);
        }
        hotelRepository.insert_images(images,descriptions,Hotel_id);
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
        @SuppressWarnings("Duplicated code fragment (9 lines long)")
        List<HotelImagesDto> hotelImagesDtoList = new ArrayList<>();
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String description = (descriptions != null && i < descriptions.size()) ? descriptions.get(i) : null;
            byte[] imageBytes = image.getBytes();

            HotelImagesDto hotelImagesDto = new HotelImagesDto(imageBytes, description);
            hotelImagesDtoList.add(hotelImagesDto);
        }
        return hotelImagesDtoList;
    }
    public void delete_image(List<Integer> ids){
        hotelRepository.delete_image(ids);
    }
}
