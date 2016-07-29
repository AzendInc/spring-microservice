package com.azendinc.billassist.security.eventlisteners;

import com.azendinc.billassist.dataservices.IUserService;
import com.azendinc.billassist.security.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Event listener for the AuthenticationSuccessEvent which is
 * fired when a login is successful
 *
 * Current Activities on Event:
 * 1) clear access denied count
 * 2) log successful login with ip address
 */
@Component
public class AuthenticationSuccessEventListener implements ApplicationListener<AuthenticationSuccessEvent> {
    @Autowired
    private IUserService userService;

    @Autowired
    private HttpServletRequest request;

    @Override
    public void onApplicationEvent(AuthenticationSuccessEvent event) {
        Authentication auth = event.getAuthentication();
        Object principal = auth.getPrincipal();
        if (principal instanceof CurrentUser) {
            String userId = ((CurrentUser)principal).getId();
            userService.clearAccessDenied(userId);
            String ipAddress = request.getRemoteAddr();
            userService.processSuccessfulAuthentication(userId, ipAddress);
        }
    }
}
