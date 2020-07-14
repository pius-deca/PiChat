package com.github.pius.pichats.service;

import com.github.pius.pichats.model.ProfilePic;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {
  Object uploadProfile(ProfilePic pic, HttpServletRequest request);
  ProfilePic find(String pic, HttpServletRequest request);
  void delete(String profile, HttpServletRequest request) throws Exception;
}
