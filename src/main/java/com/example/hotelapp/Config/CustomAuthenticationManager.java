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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * This is my custom authentication manager
 */
@Slf4j
public class CustomAuthenticationManager extends ProviderManager {
    private final DaoAuthenticationProvider defaultProvider;
    private final Map<String, DaoAuthenticationProvider> providerMap;

    public CustomAuthenticationManager(List<AuthenticationProvider> providers, DaoAuthenticationProvider adminProvider, DaoAuthenticationProvider userProvider) {
        super(providers);
        this.defaultProvider = userProvider; // Set default provider (userProvider in this case)
        this.providerMap = new HashMap<>();
        this.providerMap.put("/admin", adminProvider);
        this.providerMap.put("/user", userProvider);
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        HttpServletRequest request = ((ServletRequestAttributes) Objects.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
        String requestUri = request.getRequestURI();
        log.info("Started authentication...");
        DaoAuthenticationProvider provider = determineUserDetailsService(requestUri);
        return provider.authenticate(authentication);
    }

    /**
     * Chooses which dao provider to use
     * @param requestUri    where the request is coming from
     * @return  authentication provider based on uri
     */
    private DaoAuthenticationProvider determineUserDetailsService(String requestUri) {
        for (Map.Entry<String, DaoAuthenticationProvider> entry : providerMap.entrySet()) {
            if (requestUri.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        // Return the default provider if no match is found
        return defaultProvider;
    }
}

