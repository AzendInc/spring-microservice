package com.azendinc.billassist.security;

import com.azendinc.billassist.config.ConfigPropertyNames;
import com.azendinc.billassist.models.Role;
import com.azendinc.billassist.models.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * This class wraps the base user class built into spring security
 * It adds our logic for specifying if the account is locked and allows us
 * to reference the id as the primary identifying element. It also holds
 * a copy of the database user record that is currently logged in
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {
    private User user;

    // the reason this has an integer assocated with it is that it is read from the config
    // the value is the default if the config is not specified. This cannot be static because
    // of how the @Value works
    @Value(ConfigPropertyNames.ACCOUNT_LOCKOUT_DURATION)
    private int lockoutDuration = 30;

    /**
     * Constructor builds itself from a database user object
     * @param user the current logged in user database object
     */
    public CurrentUser(User user) {
        super(user.getEmailAddress(), user.getPasswordHash(), AuthorityUtils.createAuthorityList(user.getRole().toString()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return user.getId().toString();
    }

    public UUID getUuid() {
        return user.getId();
    }

    public Role getRole() {
        return user.getRole();
    }

    @Override
    public boolean isEnabled() {
        return user.isActive();
    }

    @Override
    public boolean isAccountNonLocked() {
        Boolean isNonLocked = true;
        Date lockedAccountOn = user.getLockedAccountOn();
        if (lockedAccountOn != null)
        {
            Date expirationDate = getExpirationDate(lockedAccountOn);
            isNonLocked = Calendar.getInstance().getTime().after(expirationDate);
        }

        return isNonLocked;
    }

    /**
     * calculates the expiration date of the account lock
     * @param lockedAccountOn the date and time the account was locked
     * @return the date and time the lock is lifted
     */
    private Date getExpirationDate(Date lockedAccountOn) {
        long lockoutDurationInMillis = TimeUnit.MINUTES.toMillis(lockoutDuration);

        return new Date(lockedAccountOn.getTime() + lockoutDurationInMillis);
    }
}
