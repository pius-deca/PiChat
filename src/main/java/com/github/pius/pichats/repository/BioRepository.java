package com.github.pius.pichats.repository;

import java.util.Optional;

import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BioRepository extends JpaRepository<Bio, Long> {
  Optional<Bio> findByUser(User user);
}
