package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Bio;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface BioRepository extends JpaRepository<Bio, Long> {
  Optional<Bio> findByUser(User user);
}
