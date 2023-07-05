package com.example.hotelapp.Service.AdminService;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import com.example.hotelapp.DTO.Admin.AdminDto;
import com.example.hotelapp.Repository.impl.AdminRepositoryImpl;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;

public class AdminDetailsService implements AuthenticationProvider {
    private final AdminRepositoryImpl adminRepository;
//    private AdminCredential adminCredential;

    public AdminDetailsService(AdminRepositoryImpl adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        AdminDto adminDto = adminRepository.get_Admin_credentials(username);
        if(adminDto == null){
            throw new BadCredentialsException("Invalid username");
        }
        if(!password.equals(adminDto.getPassword())){
            throw new BadCredentialsException("Invalid password");
        }
        //create admin credentials object
        AdminCredential adminCredential = (AdminCredential) User.withUsername(adminDto.getUsername())
                .password(adminDto.getPassword())
                .build();
        return new UsernamePasswordAuthenticationToken(adminCredential,password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
/*
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AdminDto adminDto = adminRepository.get_Admin_credentials(username);
        if (adminDto == null){
            throw new UsernameNotFoundException("No account found");
        }
        String username1 = adminDto.getUsername();
        return org.springframework.security.core.userdetails.User
                .withUsername(username1)
                .password(adminDto.getPassword())
                .roles("ROLE_ADMIN")
                .build();
    }*/
}
