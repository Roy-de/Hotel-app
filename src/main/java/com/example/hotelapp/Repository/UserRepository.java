package com.example.hotelapp.Repository;

import com.example.hotelapp.DTO.UserDto;
import com.example.hotelapp.Mappers.userRowMapper;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository {
    @Query(value = "CALL public.insert_user_record(?,?,?,?)")
    void create_user_account(UserDto userDto);
    @Query(value = "SELECT * FROM public.get_user_credentials(?)",rowMapperClass = userRowMapper.class)
    UserDto get_user_credentials(String credentials);
}
