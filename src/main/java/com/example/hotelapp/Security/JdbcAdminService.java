package com.example.hotelapp.Security;

import com.example.hotelapp.DTO.CredentialsDto;
import com.example.hotelapp.Mappers.CredentialsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


@Service
public class JdbcAdminService implements UserDetailsService {

    private final JdbcTemplate jdbcTemplate;
    @Autowired
    public JdbcAdminService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        CredentialsDto admin = get_admin_credentials(username);
        if(admin == null){
            throw new UsernameNotFoundException("Account with "+ username +" not exist");
        }else{
            return User.withUsername(admin.getUsername()).password(admin.getPassword())
                    .authorities("ADMIN").build();
        }
    }
    private CredentialsDto get_admin_credentials(String credentials){
        String sql = "SELECT id,username,email,password FROM public.admin_acc WHERE username = ? OR email = ?";

        return jdbcTemplate.queryForObject(sql,new CredentialsMapper(),credentials,credentials);
    }
}
