package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  Page<Comment> findByPostOrderByCreatedAtDesc(Pageable pageable, Post post);
  int countCommentsByPost(Post post);
}
