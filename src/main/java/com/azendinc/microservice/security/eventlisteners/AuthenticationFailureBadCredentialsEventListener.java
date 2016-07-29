package com.azendinc.billassist.security.eventlisteners;

import com.azendinc.billassist.dataservices.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationFailureBadCredentialsEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

/**
 * Event listener for the AuthenticationFailureBadCredentialsEvent
 * This event is triggered when the username and password credentials
 * are not correct
 */
@Component
public class AuthenticationFailureBadCredentialsEventListener implements ApplicationListener<AuthenticationFailureBadCredentialsEvent> {
    @Autowired
    private IUserService userService;

    @Override
    public void onApplicationEvent(AuthenticationFailureBadCredentialsEvent event) {
        Authentication auth = event.getAuthentication();
        String username = auth.getName();
        userService.processAccessDenied(username);
    }
}
