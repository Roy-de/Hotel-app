package com.example.hotelapp.SecurityTest;

import com.example.hotelapp.DTO.Admin.AdminCredential;
import com.example.hotelapp.Mappers.AdminCredentialsMapper;
import com.example.hotelapp.Security.JdbcAdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JdbcAdminServiceTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    private JdbcAdminService adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminService = new JdbcAdminService(jdbcTemplate);
    }

    @Test
    void loadUserByUsername_existingUsername_returnUserDetails() {
        String username = "admin123";
        AdminCredential admin = new AdminCredential();
        admin.setAdmin_username(username);
        admin.setAdmin_password("password123");

        when(jdbcTemplate.queryForObject(anyString(), any(AdminCredentialsMapper.class), eq(username), eq(username)))
                .thenReturn(admin);

        UserDetails userDetails = adminService.loadUserByUsername(username);

        assertNotNull(userDetails);
        assertEquals(username, userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN")));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(AdminCredentialsMapper.class), eq(username), eq(username));
    }

    @Test
    void loadUserByUsername_nonExistingUsername_throwUsernameNotFoundException() {
        String username = "nonexistinguser";

        when(jdbcTemplate.queryForObject(anyString(), any(AdminCredentialsMapper.class), eq(username), eq(username)))
                .thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> adminService.loadUserByUsername(username));

        verify(jdbcTemplate, times(1)).queryForObject(anyString(), any(AdminCredentialsMapper.class), eq(username), eq(username));
    }

}
