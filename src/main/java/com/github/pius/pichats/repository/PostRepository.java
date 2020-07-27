package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
  Optional<Post> findByPost(String post);
  Page<Post> findAllByUserOrderByCreatedAtDesc(Pageable page, User user);
  Page<Post> findAllByOrderByCreatedAtDesc(Pageable page);
  int countPostsByUser(User user);
}
