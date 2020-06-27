package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  Optional<Post> findByPost(String post);
  List<Post> findAllByUserOrderByCreatedAtDesc(User user);
  List<Post> findAllByOrderByCreatedAtDesc();
  int countPostsByUser(User user);
}
