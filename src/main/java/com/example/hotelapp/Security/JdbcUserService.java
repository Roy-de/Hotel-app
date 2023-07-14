package com.example.hotelapp.Security;

import com.example.hotelapp.DTO.User.UserCredentials;
import com.example.hotelapp.Mappers.userRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
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
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("USER"));
        return new org.springframework.security.core.userdetails.User(
                user.getUser_username(),
                user.getUser_password(),
                authorities
        );
    }
    private UserCredentials get_user_credentials(String credentials){
        String sql = "SELECT username,email,password FROM public.admin_acc WHERE username = ? OR email = ?";

        return jdbcTemplate.queryForObject(sql,new userRowMapper(),credentials);
    }
}
