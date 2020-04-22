package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
