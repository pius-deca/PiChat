package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.ChangePasswordDTO;
import com.github.pius.pichats.dto.SearchUsernameDto;
import com.github.pius.pichats.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
  User searchByUsername(String username, HttpServletRequest request);
  String changePassword(ChangePasswordDTO passwordDTO, HttpServletRequest request);
}
