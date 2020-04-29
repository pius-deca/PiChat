package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Follow;

import javax.servlet.http.HttpServletRequest;

public interface FollowService {
  Follow follow(String username, HttpServletRequest request);
  String unFollow(String username, HttpServletRequest request);
  int countFollowers(HttpServletRequest request);
  int countFollowing(HttpServletRequest request);
}
