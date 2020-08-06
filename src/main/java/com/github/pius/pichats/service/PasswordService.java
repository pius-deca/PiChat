package com.github.pius.pichats.service;

import com.github.pius.pichats.dto.ForgotPassDTO;
import com.github.pius.pichats.dto.ResetPasswordDTO;

public interface PasswordService {
  String forgotPassword(ForgotPassDTO forgotPassDTO);
  String resetPassword(ResetPasswordDTO resetPasswordDTO, String token);
}
