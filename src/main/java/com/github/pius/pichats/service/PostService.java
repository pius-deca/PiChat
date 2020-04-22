package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

public interface PostService {
  Post post(Post post, HttpServletRequest request);
  Post findPost(String post, HttpServletRequest request);
  List<Post> findAll(HttpServletRequest request);
}
