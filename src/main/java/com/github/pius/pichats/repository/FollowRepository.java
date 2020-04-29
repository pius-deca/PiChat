package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowing(String username);
  int countFollowingByUser(User user);
  int countFollowersByFollowing(String user);
}
