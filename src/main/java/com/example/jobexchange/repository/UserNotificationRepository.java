package com.example.jobexchange.repository;

import com.example.jobexchange.domain.UserNotification;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserNotificationRepository extends JpaRepository<UserNotification, Long> {
    @EntityGraph(attributePaths = "recipient")
    List<UserNotification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId);
}
