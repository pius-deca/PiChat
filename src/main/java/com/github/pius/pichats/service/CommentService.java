package com.github.pius.pichats.service;

import javax.servlet.http.HttpServletRequest;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.service.Utils.PageResultConverter;

public interface CommentService {
  Comment makeComment(String post, Comment comment, HttpServletRequest request);

  PageResultConverter getAllCommentsForAPost(int page, int limit, String post, HttpServletRequest request);

  int countPostComments(String post, HttpServletRequest request);
}
