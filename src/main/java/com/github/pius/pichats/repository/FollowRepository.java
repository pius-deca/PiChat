package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowing(String username);
}
