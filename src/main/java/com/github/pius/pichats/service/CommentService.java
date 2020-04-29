package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Comment;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CommentService {
  Comment makeComment(String post, Comment comment, HttpServletRequest request);
  List<Comment> getAllCommentsForAPost(String post, HttpServletRequest request);
  int countPostComments(String post, HttpServletRequest request);
}
