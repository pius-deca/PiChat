package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.PasswordToken;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PasswordTokenRepository extends JpaRepository<PasswordToken, Long> {
  void deleteByUser(User user);
  Optional<PasswordToken> findByToken(String code);

}
