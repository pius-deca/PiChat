package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PostService {
  Post post(Post post, HttpServletRequest request);
  Post findPost(String post, HttpServletRequest request);
  Post getPost(String post);
  List<Post> findAll(HttpServletRequest request);
  void delete(String post, HttpServletRequest request) throws Exception;
  void deleteAll(HttpServletRequest request);
}
