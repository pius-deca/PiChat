package com.github.pius.pichats.service;

import com.github.pius.pichats.model.ProfilePic;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface ProfileService {
  Object uploadProfile(MultipartFile pic, HttpServletRequest request);
  ProfilePic find(HttpServletRequest request);
  void delete(String profile, HttpServletRequest request) throws Exception;
}
