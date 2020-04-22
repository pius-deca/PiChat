package com.github.pius.pichats.dto;

import lombok.Data;

@Data
public class LoginResponseDTO extends SignupRequestDTO{
  private String token;

  public LoginResponseDTO(String firstName, String lastName, String email, String username, String password) {
    super(firstName, lastName, email, username,null);
  }
}
