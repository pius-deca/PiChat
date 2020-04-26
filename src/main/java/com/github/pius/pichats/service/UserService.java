package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.SearchUsernameDto;
import com.github.pius.pichats.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {
  User searchByUsername(SearchUsernameDto username, HttpServletRequest request);
}
