package com.github.pius.pichats.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
public class SignupRequestDTO {
  @NotBlank(message = "First name cannot be empty")
  @Column(name = "first_name")
  private String firstName;

  @NotBlank(message = "Last name cannot be empty")
  @Column(name = "last_name")
  private String lastName;

  @Email(message = "Email should be valid")
  @NotBlank(message = "Email cannot be empty")
  private String email;

  @NotBlank(message = "Username cannot be empty")
  @Size(min = 8, max = 50, message = "Username must be between 8 to 50 characters long")
  private String username;

  @NotBlank(message = "Password cannot be empty")
  @Size(min = 8, max = 16, message = "Password must be between 8 to 16 characters long")
  private String password;

  public SignupRequestDTO(String firstName, String lastName, String email, String username, String password) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.username = username;
    this.password = password;
  }
}
