package com.example.jobexchange.config;

import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.repository.AppUserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class AuthContext {
    private final AppUserRepository users;

    public AuthContext(AppUserRepository users) {
        this.users = users;
    }

    public AppUser requireUser(Long userId) {
        if (userId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Login required");
        }
        return users.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid user"));
    }

    public AppUser requireRole(Long userId, UserRole role) {
        AppUser user = requireUser(userId);
        if (user.getRole() != role) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied for this role");
        }
        return user;
    }
}
