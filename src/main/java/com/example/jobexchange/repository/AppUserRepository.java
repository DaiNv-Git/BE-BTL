package com.example.jobexchange.repository;

import com.example.jobexchange.domain.AppUser;
import com.example.jobexchange.domain.UserRole;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    List<AppUser> findByRole(UserRole role);

    Optional<AppUser> findByEmail(String email);
}
