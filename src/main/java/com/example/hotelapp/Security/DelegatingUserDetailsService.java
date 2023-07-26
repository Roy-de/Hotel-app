package com.example.hotelapp.Security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Objects;
/**
 * This class is responsible for delegating the implementation of user details service based on
 * the request url. i.e. Admin service is used if the request url is passed from the <code>"/admin/"</code>
 * code and <code>"/user/"</code> is used for user service
 */
@Service
@Slf4j
public class DelegatingUserDetailsService implements UserDetailsService {
    private final JdbcUserService userService;
    private final JdbcAdminService adminService;

    public DelegatingUserDetailsService(JdbcUserService userService, JdbcAdminService adminService) {
        this.userService = userService;
        this.adminService = adminService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUri = request.getRequestURI();
        if (requestUri.contains("admin")) {
            log.info("Using admin service in delegating user service");
            return adminService.loadUserByUsername(username);
        } else {
            log.info("Using user service in delegating user service");
            return userService.loadUserByUsername(username);
        }
    }
}
