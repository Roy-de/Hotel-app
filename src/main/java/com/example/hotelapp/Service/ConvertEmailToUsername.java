package com.example.hotelapp.Service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class ConvertEmailToUsername {
    private final JdbcTemplate jdbcTemplate;

    public ConvertEmailToUsername(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
    public String adminEmail(String email){
        String sql = "SELECT username from public.admin_acc WHERE email = ? OR username = ?";
        return jdbcTemplate.queryForObject(sql, String.class,email,email);
    }
    public String userEmail(String email){
        String sql = "SELECT username from public.user_account WHERE email = ? OR username = ?";
        return jdbcTemplate.queryForObject(sql,String.class,email,email);
    }
}
