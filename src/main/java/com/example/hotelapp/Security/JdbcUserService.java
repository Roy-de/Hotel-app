package com.example.hotelapp.Security;

import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.Mappers.userRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JdbcUserService implements UserDetailsService {
    private final JdbcTemplate jdbcTemplate;

    public JdbcUserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredentials user = get_user_credentials(username);
        if(user == null){
            throw new UsernameNotFoundException("Account does not exist");
        }
        return User.withUsername(user.getUser_username()).password(user.getUser_password()).authorities("USER").build();
    }
    private UserCredentials get_user_credentials(String credentials){
        String sql = "SELECT id,username,email,password FROM public.user_account WHERE username = ? OR email = ?";

        return jdbcTemplate.queryForObject(sql,new userRowMapper(),credentials,credentials);
    }
}
