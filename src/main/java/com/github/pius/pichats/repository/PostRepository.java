package com.github.pius.pichats.repository;

import com.github.pius.pichats.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
