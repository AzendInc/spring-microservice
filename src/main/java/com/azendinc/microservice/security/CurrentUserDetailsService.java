package com.azendinc.billassist.security;

import com.azendinc.billassist.models.User;
import com.azendinc.billassist.repositories.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * This overrides the default user details service allowing us to customize
 * the code that fetches the user from the database for the process
 * of authentication
 */
@Service
public class CurrentUserDetailsService implements UserDetailsService {
    private final IUserRepository repo;

    @Autowired
    public CurrentUserDetailsService(IUserRepository repo)
    {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User result = repo.findOneByEmailAddress(email).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with email=%s was not found", email)));

        return new CurrentUser(result);
    }
}
