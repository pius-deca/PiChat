package com.github.pius.pichats.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {

  @NotBlank(message = "This field cannot be blank")
  private String identifier;
  @NotBlank(message = "This field cannot be blank")
  private String password;
}
