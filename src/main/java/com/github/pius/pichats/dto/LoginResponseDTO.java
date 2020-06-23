package com.github.pius.pichats.dto;

import com.github.pius.pichats.model.User;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class LoginResponseDTO {
  private String firstName;
  private String lastName;
  private String email;
  private String username;
  private String password;
  private String token;

  public LoginResponseDTO(String firstName, String lastName, String email, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = null;
  }
}
