package com.example.hotelapp.Service.impl;

import com.example.hotelapp.Entity.Roles.Role;
import com.example.hotelapp.Entity.User.User;
import com.example.hotelapp.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    UserRepository userRepository;
    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if(user != null){
            return new org.springframework.security.core.userdetails.User(user.getUsername()
                    ,user.getPassword()
            ,mapRolestoAuthorities(user.getRoles()));
        }else{
            throw new UsernameNotFoundException("No username nigga!");
        }
    }

    private Collection<? extends GrantedAuthority> mapRolestoAuthorities(List<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}
