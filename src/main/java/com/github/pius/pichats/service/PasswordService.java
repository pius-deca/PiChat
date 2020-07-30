package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.ResetPasswordDTO;

public interface PasswordService {
  String forgotPassword(String identifier);
  void resetPassword(ResetPasswordDTO resetPasswordDTO, String token);
}
