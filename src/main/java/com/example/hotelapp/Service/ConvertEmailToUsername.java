package com.example.hotelapp.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConvertEmailToUsername {
    private static final String ADMIN_EMAIL = "SELECT username from public.admin_acc WHERE email = ? OR username = ?";
    private static final String USER_EMAIL = "SELECT username from public.user_account WHERE email = ? OR username = ?";
    private final JdbcTemplate jdbcTemplate;

    public ConvertEmailToUsername(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public String adminEmail(String email){
        return jdbcTemplate.queryForObject(ADMIN_EMAIL, String.class,email,email);
    }
    public String userEmail(String email){
        return jdbcTemplate.queryForObject(USER_EMAIL,String.class,email,email);
    }
}
