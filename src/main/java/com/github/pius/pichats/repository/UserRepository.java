package com.github.pius.pichats.repository;

import java.util.List;
import java.util.Optional;

import com.github.pius.pichats.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByEmail(String email);

  Optional<User> findByUsername(String username);

  String search = "from users u WHERE LOWER(u.username) LIKE lower(concat(:username,'%'))";

  @Query(search)
  List<User> searchByUsername(String username);

  boolean existsByEmail(String email);

  boolean existsByUsername(String username);
}
