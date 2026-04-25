package com.example.jobexchange.controller;

import com.example.jobexchange.config.AuthContext;
import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.UserRole;
import com.example.jobexchange.dto.CreateUserRequest;
import com.example.jobexchange.dto.UserResponse;
import jakarta.validation.Valid;
import com.example.jobexchange.repository.AppUserRepository;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final AppUserRepository users;
    private final AuthContext authContext;

    public UserController(AppUserRepository users, AuthContext authContext) {
        this.users = users;
        this.authContext = authContext;
    }

    @GetMapping
    public List<UserResponse> list(@RequestParam(required = false) UserRole role, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        authContext.requireRole(userId, UserRole.ADMIN);
        return (role == null ? users.findAll() : users.findByRole(role))
                .stream()
                .map(UserResponse::from)
                .toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@Valid @RequestBody CreateUserRequest request, @RequestHeader(value = "X-User-Id", required = false) Long userId) {
        authContext.requireRole(userId, UserRole.ADMIN);
        AppUser user = new AppUser(
                request.fullName(),
                request.email(),
                request.phone(),
                request.password() == null || request.password().isBlank() ? "123456" : request.password(),
                request.role(),
                request.organizationName(),
                request.headline()
        );
        return UserResponse.from(users.save(user));
    }
}
