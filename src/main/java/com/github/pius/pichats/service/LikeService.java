package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Like;

import javax.servlet.http.HttpServletRequest;

public interface LikeService {
  Like likeOrUnlike(String post, HttpServletRequest request);

}
