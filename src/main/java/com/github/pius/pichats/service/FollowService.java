package com.github.pius.pichats.service;

import com.github.pius.pichats.model.Follow;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

public interface FollowService {
  String isFollowing(String username, HttpServletRequest request);

  Follow follow(String username, HttpServletRequest request);

  Follow acceptRequest(String username, HttpServletRequest request);

  String declineRequest(String username, HttpServletRequest request);

  String unFollow(String username, HttpServletRequest request);

  List<Follow> listOfFollowers(String username, HttpServletRequest request);

  List<Follow> listOfFollowing(String username, HttpServletRequest request);

  int countFollowingSearchedUser(String username, HttpServletRequest request);

  int countFollowersOfSearchedUser(String username, HttpServletRequest request);
}
