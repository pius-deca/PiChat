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
  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, max = 16, message = "Password must be between 8 to 16 characters long")
  private String password;

  @NotBlank(message = "Confirm password cannot be empty")
  private String confirmPassword;

}
