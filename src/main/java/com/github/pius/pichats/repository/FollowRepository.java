package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Follow;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {
  Optional<Follow> findByFollowingAndUser(String username, User user);

  Optional<Follow> findByUser(User user);

  int countFollowingByUserAndAccepted(User user, boolean accepted);

  int countFollowersByFollowingAndAccepted(String following, boolean accepted);

  List<Follow> findAllByFollowing(String username);

  List<Follow> findAllByFollowingAndAccepted(String username, boolean accepted);

  List<Follow> findAllByUserAndAccepted(User user, boolean accepted);
}
