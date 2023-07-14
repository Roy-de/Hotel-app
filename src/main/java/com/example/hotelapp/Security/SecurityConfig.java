package com.example.hotelapp.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JdbcAdminService jdbcAdminService;
    @SuppressWarnings(value = "Private field 'jdbcUserService' is assigned but never accessed")
    private final JdbcUserService jdbcUserService;
    @Autowired
    public SecurityConfig(JdbcAdminService jdbcAdminService, JdbcUserService jdbcUserService) {
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
    @Autowired
    public void configure(AuthenticationManagerBuilder managerBuilder){
        managerBuilder.authenticationProvider(adminProvider());
        // managerBuilder.userDetailsService(jdbcUserService).passwordEncoder(encoder());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http.cors(Customizer.withDefaults()).csrf(AbstractHttpConfigurer::disable).
                authorizeHttpRequests(requests -> requests
                        .requestMatchers("/admin/hello").authenticated()
                        .anyRequest().permitAll())
                .httpBasic(Customizer.withDefaults())
                //.addFilter(adminAuthenticationFilter())
                .build();
    }
    /*@Bean
    public AdminAuthFilter adminAuthenticationFilter() throws Exception {
        AdminAuthFilter filter = new AdminAuthFilter();
    }*/

    public DaoAuthenticationProvider adminProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(jdbcAdminService);
        authenticationProvider.setPasswordEncoder(encoder());
        return authenticationProvider;
    }

    private PasswordEncoder encoder(){
        return  new BCryptPasswordEncoder();
    }
}
