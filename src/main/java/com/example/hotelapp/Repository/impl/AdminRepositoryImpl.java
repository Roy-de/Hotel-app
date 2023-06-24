package com.example.hotelapp.Repository.impl;

import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.Repository.AdminRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

@Repository
public class AdminRepositoryImpl implements AdminRepository {
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
    private final JdbcTemplate jdbcTemplate;

    public AdminRepositoryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void create_admin_account(AdminDto adminDto) {
        String sql = "INSERT INTO public.admin_acc(username,email,password) VALUES (?,?,?)";
        Object[] params = {
                adminDto.getUsername(),
                adminDto.getEmail(),
                passwordEncoder.encode(adminDto.getPassword())
        };
        jdbcTemplate.update(sql,params);
    }

    @Override
    public AdminDto get_Admin_credentials(AdminDto adminDto) {
        return null;
    }

}
