package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Post;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface PostService {
  Object post(Post post, HttpServletRequest request);
  Post findPost(String post, HttpServletRequest request);
  Post getPost(String post);
  List<Post> findAllPostsByUser(String username, HttpServletRequest request);
  List<Post> findAll(HttpServletRequest request);
  void delete(String post, HttpServletRequest request) throws Exception;
  String selectPostToDelete(String post, HttpServletRequest request);
  String clearBatchDelete(HttpServletRequest request);
  String batchDelete(HttpServletRequest request) throws Exception;
  int countPostsOfUser(String username, HttpServletRequest request);
}
