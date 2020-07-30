package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResetPasswordDTO {
  @NotBlank(message = "password must be valid")
  private String password;

  @NotBlank(message = "Confirm new password")
  private String confirmPassword;

}
