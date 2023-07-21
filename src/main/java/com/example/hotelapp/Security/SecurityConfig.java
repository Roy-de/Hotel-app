package com.example.hotelapp.Security;

import com.example.hotelapp.Config.CustomAuthenticationManager;
import com.example.hotelapp.Config.JwtAuthFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@Slf4j
public class SecurityConfig{
    private final JwtAuthFilter jwtAuthFilter;
    private final JdbcAdminService jdbcAdminService;
    private final JdbcUserService jdbcUserService;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JdbcAdminService jdbcAdminService, JdbcUserService jdbcUserService) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jdbcAdminService = jdbcAdminService;
        this.jdbcUserService = jdbcUserService;
    }
    @Bean
    public CorsConfigurationSource corsConfigurer(){
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedHeader("*");
        configuration.addAllowedOrigin("*");
        configuration.addAllowedMethod("*");
        configuration.addAllowedOriginPattern("*");

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**",configuration);
        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin/**").hasAnyAuthority("ADMIN")
                        .requestMatchers("/user/**").hasAnyAuthority("USER")
                        .requestMatchers("/login/**").permitAll()
                        .requestMatchers("/hotel/**").permitAll()
                        .anyRequest().authenticated())
                .sessionManagement((session)->session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationManager(authenticationManager())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults())
                .logout((logout)->logout.logoutUrl("/logout"));
        return http.build();
    }

    @Bean
    public CustomAuthenticationManager authenticationManager(){
        List<AuthenticationProvider> authenticationProviders = Arrays.asList(adminProvider(),userProvider());
        return new CustomAuthenticationManager(authenticationProviders,adminProvider(),userProvider());
    }
    @Bean
    public DaoAuthenticationProvider adminProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jdbcAdminService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }
    @Bean
    public DaoAuthenticationProvider userProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jdbcUserService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }
    private PasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }
}
