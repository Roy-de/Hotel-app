package com.example.hotelapp.Security;

import com.example.hotelapp.DTO.CredentialsDto;
import com.example.hotelapp.Mappers.CredentialsMapper;
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
        CredentialsDto user = get_user_credentials(username);
        if(user == null){
            throw new UsernameNotFoundException("Account does not exist");
        }
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                .authorities("USER").build();
    }
    private CredentialsDto get_user_credentials(String credentials){
        String sql = "SELECT id,username,email,password FROM public.user_account WHERE username = ? OR email = ?";

        return jdbcTemplate.queryForObject(sql,new CredentialsMapper(),credentials,credentials);
    }
}
