package com.example.hotelapp.Config;

import com.example.hotelapp.Security.JdbcAdminService;
import com.example.hotelapp.Security.JdbcUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;
@Slf4j
public class CustomAuthenticationManager extends ProviderManager {
    private final UserDetailsService userService;
    private final UserDetailsService adminService;
    public CustomAuthenticationManager(List<AuthenticationProvider> providers, JdbcUserService userService, JdbcAdminService adminService) {
        super(providers);
        this.userService = userService;
        this.adminService = adminService;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUri = request.getRequestURI();

        UserDetails userDetails = determineUserDetailsService(requestUri).loadUserByUsername(authentication.getName());
        PreAuthenticatedAuthenticationToken token = new PreAuthenticatedAuthenticationToken(userDetails,null,userDetails.getAuthorities());

        token.setDetails(authentication.getDetails());
        log.info("Starting authentication for user: {}", userDetails.getUsername());

        log.info("Using authentication: {}",token);
        log.info("Authentication successful for user: {}", userDetails.getUsername());
        return token;
    }

    private UserDetailsService determineUserDetailsService(String requestUri) {
        if(requestUri.contains("admin")){
            log.debug("Using admin service for authentication");
            return adminService;
        }
        else{
            log.debug("Using user service for authentication");
            return userService;
        }
    }
}
