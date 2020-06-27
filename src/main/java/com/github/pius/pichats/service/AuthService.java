package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.dto.SignupRequestDTO;
import com.github.pius.pichats.model.User;
import org.springframework.mail.MailSendException;

import javax.servlet.http.HttpServletRequest;

public interface AuthService {
  User register(SignupRequestDTO user) throws Exception;
  String activate(String code);
  LoginResponseDTO login(LoginRequestDTO user);
}
