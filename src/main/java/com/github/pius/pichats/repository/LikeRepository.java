package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Like;
import com.github.pius.pichats.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<Like, Long> {
  Optional<Like> findByPost(Post post);
  int countLikesByPost(Post post);
}
