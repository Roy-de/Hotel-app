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
    private final ThreadLocal<String> currentUsername = new ThreadLocal<>();

    public JwtAuthFilter(JwtService jwtService, DelegatingUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }
    public String getUsername(){
        return currentUsername.get();
    }

    /**
     * Filter for JWT
     * @param request HttpRequest
     * @param response  HttpResponse
     * @param filterChain   Filter chain
     * @throws ServletException Servlet exception
     * @throws IOException  IOException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, @Nonnull HttpServletResponse response,@Nonnull FilterChain filterChain) throws ServletException, IOException {
        String authHeader =  request.getHeader("Authorization");
        String username =null;
        String token = null;
        if(authHeader != null && authHeader.startsWith("Bearer ")){
            token = authHeader.substring(7);
            username = jwtService.extractUsername(token);
        }
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            if(jwtService.validateToken(token,userDetails)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null,userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
                currentUsername.set(username);
            }
        }
        filterChain.doFilter(request,response);
    }
}
