package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.LoginRequestDTO;
import com.github.pius.pichats.dto.LoginResponseDTO;
import com.github.pius.pichats.model.User;
import org.springframework.mail.MailSendException;

public interface AuthService {
  User register(User user) throws MailSendException;
  String activate(String code);
  LoginResponseDTO login(LoginRequestDTO user);
}
