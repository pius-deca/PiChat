package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.PostDTO;
import com.github.pius.pichats.model.Post;
import com.github.pius.pichats.service.Utils.PageResultConverter;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface PostService {
  Object post(PostDTO caption, MultipartFile file, HttpServletRequest request);

  Post findPost(String post, HttpServletRequest request);

  Post getPost(String post);

  PageResultConverter findAllPostsByUser(int page, int limit, String username, HttpServletRequest request);

  PageResultConverter findAll(int page, int limit, HttpServletRequest request);

  void delete(String post, HttpServletRequest request) throws Exception;

  String selectPostToDelete(String post, HttpServletRequest request);

  String clearBatchDelete(HttpServletRequest request);

  String batchDelete(HttpServletRequest request) throws Exception;

  int countPostsOfUser(String username, HttpServletRequest request);
}
