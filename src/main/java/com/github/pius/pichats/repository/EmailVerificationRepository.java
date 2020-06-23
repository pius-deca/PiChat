package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {
   public Optional<EmailVerification> findByCode(String code);
}