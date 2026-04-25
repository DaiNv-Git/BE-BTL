package com.example.jobexchange.dto;

import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.UserRole;

public record UserResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        UserRole role,
        String organizationName,
        String headline
) {
    public static UserResponse from(AppUser user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getPhone(),
                user.getRole(),
                user.getOrganizationName(),
                user.getHeadline()
        );
    }
}
