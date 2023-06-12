package com.example.hotelapplication.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
/*    private UserDetailsService userDetailsService;

    public SecurityConfig(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }
    public SecurityConfig(){}*/
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
         return http.authorizeHttpRequests((authorize)->authorize
                .requestMatchers("/signup/**").permitAll()
                .requestMatchers("/login/**").permitAll()
                .anyRequest().authenticated()
        ).formLogin((form)->form
                 .loginPage("/login")
                 .loginProcessingUrl("/login")
                 .defaultSuccessUrl("/users")
                 .permitAll()).build();
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return  new BCryptPasswordEncoder();
    }
/*    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception{
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }*/
}
