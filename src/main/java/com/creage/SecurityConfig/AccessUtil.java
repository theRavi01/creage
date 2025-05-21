package com.creage.SecurityConfig;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AccessUtil {

    public static boolean hasReadAccess() {
        int writeaccess = getwriteaccessFromContext();
        return writeaccess >= 0; // writeaccess 0, 1, 2
    }

    public static boolean hasWriteAccess() {
        int writeaccess = getwriteaccessFromContext();
        return writeaccess >= 1; // writeaccess 1, 2
    }

    public static boolean hasAllAccess() {
        int writeaccess = getwriteaccessFromContext();
        return writeaccess == 2;
    }

    private static int getwriteaccessFromContext() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object details = (auth != null) ? auth.getDetails() : null;
        if (details instanceof Integer) {
            return (Integer) details;
        }
        return -1; // unauthorized or unknown
    }
}
