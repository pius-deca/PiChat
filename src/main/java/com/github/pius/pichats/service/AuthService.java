package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.AuthResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
  AuthResponseDTO register(SignupRequestDTO user) throws Exception;

  String activate(String code, HttpServletRequest request);

  AuthResponseDTO login(LoginRequestDTO user);
}
