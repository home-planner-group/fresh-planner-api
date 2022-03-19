package com.freshplanner.api.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityContext {
    public static String extractUsername() {
        return ((UserAuthority) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
    }
}
