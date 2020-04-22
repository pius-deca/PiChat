package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Comment;
import com.github.pius.pichats.model.Post;

import javax.servlet.http.HttpServletRequest;

public interface CommentService {
  Comment makeComment(Long postId, Comment comment, HttpServletRequest request);

}
