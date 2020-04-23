package com.github.pius.pichats.service;

import com.github.pius.pichats.model.ProfilePic;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {
  ProfilePic uploadProfile(ProfilePic pic, HttpServletRequest request);

}
