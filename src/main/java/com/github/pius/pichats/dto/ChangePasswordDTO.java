package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
  @NotBlank(message = "Current Password cannot be empty")
  private String currentPassword;

  @NotBlank(message = "New Password cannot be empty")
  private String newPassword;

  @NotBlank(message = "Confirm Password cannot be empty")
  @Size(min = 8, max = 16, message = "Password must be between 8 to 16 characters long")
  private String confirmPassword;
}
