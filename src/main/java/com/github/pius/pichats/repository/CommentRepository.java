package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  List<Comment> findByPost(Post post);
}
