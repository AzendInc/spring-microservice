package com.azendinc.billassist.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static java.util.Collections.*;

/**
 * Builder class that creates the AuthenticationProvder and allows for injection
 */
@Component
public class AuthenticationProviderBuilder {
    @Autowired
    private CurrentUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private AuthenticationEventPublisher eventPublisher;

    /**
     * Injectable bean for building an AuthenticationManager
     * @return injected authentication manager
     */
    @Bean
    private AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);

        ProviderManager providerManager = new ProviderManager(singletonList(provider));
        providerManager.setAuthenticationEventPublisher(eventPublisher);

        return providerManager;
    }
}
