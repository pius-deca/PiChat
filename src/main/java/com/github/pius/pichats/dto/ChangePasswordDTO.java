package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChangePasswordDTO {
  @NotBlank(message = "Enter old password")
  private String currentPassword;

  @NotBlank(message = "Enter new password")
  private String newPassword;

  @NotBlank(message = "Confirm new password")
  private String confirmPassword;
}
