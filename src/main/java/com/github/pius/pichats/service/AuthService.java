package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.model.User;

public interface AuthService {
  User register(User user);
  LoginResponseDTO login(LoginRequestDTO user);
}
