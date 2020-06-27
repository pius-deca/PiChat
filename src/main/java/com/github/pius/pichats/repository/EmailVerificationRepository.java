package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.EmailVerification;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
   Optional<EmailVerification> findByCodeAndUser(String code, User user);
   Optional<EmailVerification> findByUser(User user);
}