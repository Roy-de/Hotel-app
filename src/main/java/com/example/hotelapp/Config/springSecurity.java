package com.example.hotelapp.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class springSecurity {
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }
  /*  @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests((auth)->auth
                        .requestMatchers("/signup/**").permitAll()
                        .requestMatchers("/index").permitAll()
                        .requestMatchers("/users").hasRole("ADMiN"))
                .formLogin((form)->form.loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/users").permitAll())
                .logout((logout)->logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout")).permitAll()).build();
    }

    public void configureGlobal(AuthenticationManagerBuilder auth2) throws Exception{
        auth2.userDetailsService(userDetailsService)
                .passwordEncoder(encode());
    }*/
}
