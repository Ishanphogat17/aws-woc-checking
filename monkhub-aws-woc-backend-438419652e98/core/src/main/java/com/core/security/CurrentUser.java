package com.core.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;


public class CurrentUser {
    private CurrentUser(){
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
    public static UUID getId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof CustomUserDetails user) {
            return user.getId();
        }
        return null;
    }
}
