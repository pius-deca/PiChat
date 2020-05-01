package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowing(String username);
  Optional<Follow> findByUser(User user);
  int countFollowingByUserAndAccepted(User user, boolean accepted);
  int countFollowersByFollowingAndAccepted(String following, boolean accepted);
}
