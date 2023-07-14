package com.example.hotelapp.Security;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import com.example.hotelapp.Mappers.AdminCredentialsMapper;
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
        AdminCredential admin = get_admin_credentials(username);
        if(admin == null){
            throw new UsernameNotFoundException("Account with "+ username +" not exist");
        }else{
            return User.withUsername(admin.getAdmin_username()).password(admin.getAdmin_password())
                    .authorities("ADMIN").build();
        }
    }
    private AdminCredential get_admin_credentials(String credentials){
        String sql = "SELECT id,username,email,password FROM public.admin_acc WHERE username = ? OR email = ?";

        return jdbcTemplate.queryForObject(sql,new AdminCredentialsMapper(),credentials,credentials);
    }
}
