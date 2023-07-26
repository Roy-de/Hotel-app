package com.example.hotelapp.Service.ImageService;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.ApiResponse;
import com.cloudinary.utils.ObjectUtils;
import com.example.hotelapp.DTO.Hotel.HotelImagesDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class ImageServiceLayer {
    private final Cloudinary cloudinary;
    @Autowired
    public ImageServiceLayer(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }
    public HotelImagesDto uploadImage(MultipartFile image)throws IOException{
        HotelImagesDto hotelImagesDto = new HotelImagesDto();
        Map<?,?> uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String public_id = uploadResult.get("public_id").toString();
        String secure_url = uploadResult.get("secure_url").toString();
        log.info("Public ID: {}",public_id);
        log.info("Secure URL: {}",secure_url);
        hotelImagesDto.setImageUrl(secure_url);
        hotelImagesDto.setPublic_id(public_id);
        return hotelImagesDto;
    }
    public String deleteImage(List<String> public_id){
        try{
            ApiResponse response = cloudinary.api().deleteResources(public_id,ObjectUtils.emptyMap());
            return response.toString();
        }catch (Exception e){
            return e.getMessage();
        }
    }
    public void updateImage(String public_id,MultipartFile image){
        try{
            cloudinary.uploader().upload(image.getBytes(),ObjectUtils.asMap("public_id",public_id));
        }catch (Exception e){
            log.info("Error: {}",e.getMessage());
        }
    }
}
