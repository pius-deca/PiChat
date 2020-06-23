package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Follow;

import javax.servlet.http.HttpServletRequest;

public interface FollowService {
  Follow follow(String username, HttpServletRequest request);
  Follow acceptRequest(String username, HttpServletRequest request);
  String declineRequest(String username, HttpServletRequest request);
  String unFollow(String username, HttpServletRequest request);
  int countFollowers(HttpServletRequest request);
  int countFollowing(HttpServletRequest request);
  int countFollowingSearchedUser(String username, HttpServletRequest request);
  int countFollowersOfSearchedUser(String username, HttpServletRequest request);
}
