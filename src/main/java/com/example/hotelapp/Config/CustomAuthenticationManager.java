package com.example.hotelapp.Config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.List;
import java.util.Objects;

@Slf4j
public class CustomAuthenticationManager extends ProviderManager {
    private final DaoAuthenticationProvider adminProvider;
    private final DaoAuthenticationProvider userProvider;

    public CustomAuthenticationManager(List<AuthenticationProvider> providers, DaoAuthenticationProvider adminProvider, DaoAuthenticationProvider userProvider) {
        super(providers);
        this.adminProvider = adminProvider;
        this.userProvider = userProvider;
    }
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUri = request.getRequestURI();
        log.info("Started authentication");
        DaoAuthenticationProvider provider = determineUserDetailsService(requestUri);
        log.info("User: {}",authentication.getCredentials());
        return provider.authenticate(authentication);
    }

    private DaoAuthenticationProvider determineUserDetailsService(String requestUri) {
        if (requestUri.contains("admin")) {
            log.info("Using admin service for authentication");
            return adminProvider;
        } else {
            log.info("Using user service for authentication");
            return userProvider;
        }
    }
}
