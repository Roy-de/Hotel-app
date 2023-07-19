package com.example.hotelapp.Config;

import com.example.hotelapp.Security.DelegatingUserDetailsService;
import com.example.hotelapp.Service.Jwt.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Component
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final DelegatingUserDetailsService userDetailsService;

    public JwtAuthFilter(JwtService jwtService, DelegatingUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response,@Nonnull FilterChain filterChain) throws ServletException, IOException {
        String authHeader =  request.getHeader("Authorization");
        String username =null;
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            log.info("Getting token");
            token = authHeader.substring(7);
            log.info("Extracting username from token");
            username = jwtService.extractUsername(token);
            log.info("Successfully extracted username from token");
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            log.info("Loading username from delegating user service plus roles");
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            log.info("Got user details: {}",userDetails.getUsername());
            log.info("Authorities: {}",userDetails.getAuthorities());
            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                log.info("Auth token: {}",authToken.getCredentials());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                log.info("Successfully created and set authentication for user: {}",userDetails.getUsername());
            }
        }
        filterChain.doFilter(request,response);
    }
}
